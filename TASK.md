# Case Study QA
The case study contains two main parts. Please do not spend more than 3 hours in total. Please don't
be afraid to submit your work even if you feel you haven't finished it.
Scope of testing are the search API on
https://api.wikimedia.org/wiki/Core_REST_API/Reference/Search and its search results.

### Part 1

The aim of this part is to automate 2 provided scenarios. Our focus is also on the structure of the
project.
#### Scenario 1:
```
GIVEN A client of the API
WHEN A search for pages containing for ‘furry rabbits’ is executed
THEN A page with the title ‘Sesame Street’ is found
```
#### Scenario 2:
```
GIVEN The result for ‘furry rabbits’ search contains ‘Sesame Street’
WHEN The page details for Sesame Street are requested
THEN It has a latest timestamp > 2023-08-17
 ```
  Technical requirements:
- Your solution is in a public Github repository
- Your solution contains a README file with information on how to run it
- Feel free to use the programming language of your choice.


###  Part 2

  Please create your own test cases (or scenarios titles) - without automating them. You could provide
  those cases in any format. Please, describe why you decided to write those cases.
  
### Bonus (Optional)

  To create a page via that API, you need to provide an Authorization Header. Describe how you would
  adapt testing/automation?
  Assume we just created that public Wikipedia API. Describe which other aspects you would want to
  test and how.
  The team is looking forward to reviewing your solutions.