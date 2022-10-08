  Feature: Dummy Rest API Functionality Scenarios


  Scenario Outline: All endpoints return 200
    Given Get Call to "<url>"
    Then Response Code 200 is returned
    Examples:
      | url       |
      | /posts  |
      | /comments |
      | /profile  |

  Scenario Outline: Post to all endpoints
    Given Post new item to "<url>"
    And Response Code 201 is returned
    When Get Call to "<url>"
    Then new "<url>" item is returned
    Examples:
      | url       |
      | /posts  |
      | /comments |
      | /profile  |

  Scenario Outline: Delete a post and comment
    Given Post new item to "<url>" with unique ID
    And Response Code 201 is returned
    When delete item from "<url>"
    And Get Call to "<url>"
    Then new post is not returned
    Examples:
      | url       |
      | /posts    |
      | /comments |




