# Zeal QA case study by Ivan Skvortsov

Initial task description can be found [here](TASK.md).

## Part 1

### Structure

```
app
 |-> src
      |-> test
            |-> kotlin
                    |-> zeal.challenge
                                |-> objects (Kotlin objects that describe JSON)
                                |-> tests (actual test classes)
```
### Tools used

I decided to use:

1) ***Kotlin*** since I was told that Zeal developers use it for BE.

2) ***Jackson Json*** was used for working with Jsons since I used it in the past projects.
3) And ***RestAssured + Kotlin Extensions*** were used for working with API and BDD style code in tests because we talked briefly about that framework during the interview.

***Note:***  *I ended up being not really happy with how use of ***Rest Assured Kotlin Extension*** turned out because `Given` section only allows to provide configs and request parameters.*

*Another downside is that it wasn't possible to parse timestamp in `Then` section of the Scenario 2. I had to add parsing and assertion after Given/When/Then section. 
As a result, assertion processing works as expected, but I think the code could have been more readable with other tools like **Karate DSL and Kotest** that I have also considered for this task.*

### Concerns about provided scenarios

I would start with addressing my concerns about the suggested scenarios in Part 1 of [the task](TASK.md):

- Scenario 1 has a redundant Given part (*GIVEN A client of the API*) since the API client will be needed to execute any request.
- Scenario 2 suggests that Scenario 1 should be executed first since Given part says *"The result for ‘furry rabbits’ search contains ‘Sesame Street’"*. 
Upon further exploration, I noticed that [Get page request](https://api.wikimedia.org/wiki/Core_REST_API/Reference/Pages/Get_page) requires only a page name and no additional data. 
So there's no need to execute search for 'furry rabbits' request first, because Get Page request doesn't depend on any data from search response except for page name.

If it was real life situation, I would rewrite scenarios to something like that: 

#### Scenario 1:
```
GIVEN A search query string is 'furry rabbits'
WHEN A search content request is executed
THEN Response contains a page with the title ‘Sesame Street’
```

#### Scenario 2:
```
GIVEN The page title is ‘Sesame Street’
WHEN The page details are requested
THEN Response has a latest timestamp > 2023-08-17
```

But for the purpose of this Case Study I've implemented original scenarios and only omitted Given part from Scenario 2.

### How to run

Simply run `./gradlew test` from the root. 

***Note:***  *I've only added logging in case validation or assertion fails. 
If it was a real life scenario I would discuss with the team what test reports are required and would be the most useful.*

## Part 2

***Please create your own test cases (or scenarios titles) - without automating them. You could provide
those cases in any format. Please, describe why you decided to write those cases.***

I decided to follow BDD style for my test cases and pick up a few possible scenarios from
https://api.wikimedia.org/wiki/Core_REST_API/Reference/Pages/Create_page since it's discussed in Bonus part:

```
Scenario 1: User can create a new page
GIVEN User is authorized
WHEN Page create request with {new_page_data} is sent
THEN Response HTTP status code is 201
AND Response contains {new_page_data}

Scenario 2: User cannot create existing page
GIVEN User is authorized
WHEN Page create request with {existing_page_data} is sent
THEN Response HTTP status code is 409
AND Response contains edit conflict error message

Scenario 3: User cannot create a page with wrong content model
GIVEN User is authorized
WHEN Page create request with {wrong_data_for_page_creation} is sent
THEN Response HTTP status code is 400
AND Response contains content model error message

Scenario 4: User cannot create a page without auth token
GIVEN User is not authorized
WHEN Page create request with {new_page_data} is sent
THEN Response HTTP status code is 401
AND Response contains missing token error message
```

Those testcases would be handy to verify functional behavior of `Create page` endpoint:

- Make sure that User can create a page
- Make sure that it's not possible to create exact copy of existing page
- Make sure that User won't be able to create a page using erroneous content model
- Make sure that it's not possible to create a page without auth token
- Make sure that error messages are descriptive and correspond to error codes

It's possible to create such scenarios for every endpoint described in the API doc.

## Bonus (Optional)

***To create a page via that API, you need to provide an Authorization Header. Describe how you would
adapt testing/automation?***

I would follow instructions on [this page](https://api.wikimedia.org/wiki/Authentication) to be able to generate an auth token.
For starters, I would put auth token fetching to *@BeforeClass* of test classes that require authorization (like *CreatePageTest*). 
But there are many things can be done to further improve scalability and readability of the solution. 
For example, token fetching request can be put into a separate singleton class that will also store a token when it's fetched. 
This would allow getting auth token in any of the test classes and avoid sending multiple auth requests (assuming that the same auth token can be used for all tests).

***Assume we just created that public Wikipedia API. Describe which other aspects you would want to
test and how.***

I would gather information about endpoint usage statistics (or projected usage if it's a new project) and prioritized most used scenarios for automation.
Adding smoke tests into the pipeline for deployment would help to maintain quality and improve early issue detection.
It might make sense to think about scheduled full suite test runs and reporting. Reporting is very important for visibility since team needs to be aware as soon as possible about potential issues, bottlenecks and risks.
Then it might make sense to add Load/Stress testing to make sure our servers will work with a projected and increased load.

There are actually many things that can be done and improvement scope always depends on priorities, needs, and available resources.