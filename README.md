# Vanderbilt Dining Info Service (VDIS)

# Questions:

1. What are your concerns about Vanderbilt Dining?
2. Are you concerned about your allergy needs being met by Vandy dining?
3. How does time of day impact how you decide to go to a Vandy dining hall?
4. What are your expectations from a campus dining facility?
5. When you see lines at peak hours, how does that affect whether you decide to get campus dining food?
6. When a certain line is long at campus dining, do you think about going to a different location in search of shorter lines?
7. How does the overall dining experience effect your satisfaction with the food served?
8. Why do you decide to choose one dining location over another?
9. Do you care about the food selection offered at campus dining locations?
10. Does campus dining menu selection for the day impact where you go for meals?


# Answers:

## Question 1:
General consensus is that rush hour is tough to get meals

## Question 2:
Three recipients did not seem to have food allergies and did not prioritize this information.

## Question 3:
Individuals responded that they avoid heavy meal times as much as possible for times-sake.

## Question 4:
Expectations were centered around being clean, prompt, safe, and adequate portion sizes.

## Question 5:
Overall yes. Several individuals might skip meals because they saw the lengthy lines at dining facilities.

## Question 6:
Some say yes, but others choose to not eat at all.

## Question 7:
In general, most are satisfied with the quality of the food, but find that figuring out how to factor campus dining into their day decreases their satisfaction with the services offered.s

## Question 8:
This question was replied with a combination of quality of food, type of food, hours served, and peak hours

## Question 9:
All seemed to care what type of food is served at a station, but did not give much thought to the quality of food delivered. This shows a need for knowing what is where on campus (menu wise).

## Question 10:
Sometimes yes, but interviewees tended to stick to what their habits + schedules allowed.





# Requirements

When writing an application for an end user, it is critically important to ensure that you have the correct perspective of the problem to solve. Maintaining a humble and utility focused perspective during R&D helps foster a wholistic perspective of a product as a solution.

In order to best serve Vanderbilt's population with this texting service, Vanderbilt Dining Info Service (VDIS) must be able to consistently deliver accurate information regarding the dining environment at a location. This includes being able to retrieve information about what is being served and how long the lines are at any location on campus. Thus, the list below is a summary of the cornerstone requirements for the project.

* Admin users can update a menu for a location
* General users can query for a location's menu by using a format like `menu <location>`
* General users can query for a location's hours by using a format like `hours <location>`
* General users can query for a location's wait time by using a format like `wait-time <location> `
* Unavailable information can be forwarded to an admin to be answered.


# Development Approach

The most crucial part of a project is identifying and addressing the need for a product and defining the scope of the project to produce a helpful product. Having identified several user stories and interviewed multiple individuals who validate a potential need for this application, the next steps are setting out the structure necessary to actually create this application.

In order to correctly build out this application, it is imperative to identify the necessary functional systems. These systems include identifying and interacting with users (treating admin and general users appropriately according to their roles), storing information about a location for a certain applicable time period, and allowing conditional forwarding of questions to be answered if there are no answers previously available about a location.

Now that these systems have been appropriately identified, the details about the application must be roughly discussed to ensure general cohesiveness.
The structure of a user must include:
* Permissions level
* Unique identifier by phone number
* (if admin) Locations for which a user is an expert on.

It could also be useful to store a users past interactions with the service to potentially add machine learning solutions to readily identified functional dependencies within the available usage data.

While this is a user facing application, the systems must be built from inward (internal api and state tracking) to out (user interactive texting services). This helps identify the necessary core functionality that the interface must accommodate.

During development, it is crucial to continue checking in with the potential market to test theories, usability, functionality, and if the project is even still applicable or necessary. Communication with the end user will largely mitigate issues in addressing the full problem appropriately.

Estimation for this project will require frequent updates after communication with the potential market to update or push back deadlines as necessary. The overhead from clear communication is a necessary cost to ensure the market is appropriately taken care of.
