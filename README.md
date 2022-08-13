#Smart Healthcare Application#
In this era of technology, machine learning used in the health sector has been of growing importance.
Having a portable system application that could let one know how to improve one’s health has become an obsession in recent times.
In this project, an artificial intelligence therapist, an AI doctor, a few algorithms,
and supervised learning has been trained and carefully tested to improve their accuracy and efficiency.
A meal planner, based on the individual’s current body mass index, workout routine, and calories intake,
a newsstand implemented with the help of an API, and a Mapbox Maps GPS hospital locator, was implemented.
Investigations were done on the performance of the various parts of the application to prove its accuracy,
its user-friendliness, and it turned great.
Finally, we introduce how innovative healthcare applications can be applied to and benefit various healthcare fields,
including knowledge discovery, diagnosis, and social development.

the application starts with a spalsh screen then moves to a dashboard which is quite a sight for the eyesore.
users will be allowed to navigate their way through the dashboard without having to create an account.
the log in and sign up components can be found on the navigation bar,
the sorage of the user data is done with firebase real time storage system.
main components as mentioned in the introductory part above are;

*1. AI Therapist*
The therapist uses Google’s Dialogflow. Dialogflow uses many natural languages processing models to make a chatbot in a cloud.
Dialog flow helps shape the chatbot to interact with the users using behavioral cognition therapy.

*2. AI Medical Assistant*
the development of an AI Disease Prediction Medical Assistant allows the application to predict user diseases and discomforts.
Infermedica is the API on which the assistant operates.
Infermedica develops AI tools for triage and preliminary medical diagnosis and tackles inappropriate use of medical services and misdiagnosis

*3. Meal Planner*
In order to produce a diet planner, a meal planner is incorporated into the application.
This application takes five separate inputs.
That is the users’ age, height, weight, gender, and workout activity level.
Based on the answers given by the user, the diet plan project will calculate the users’ BMI, BMR and then produce a diet plan

*4. Newsstand*
Further, the application is featured as a source of health information.
The data that feeds this news feature is sourced from health news and technology portal.
In this part of the application, News API, retrofit and picasso are used

*5. GPS Locator*
In order to enable users to locate nearby health facilities, 
a geographical map feature-based GPS locator was incorporated in the application. 
The development of the geographical map portal is to locate the nearest hospital within the user’s location. 
The nearest position of hospitals is calculated with a built-in feature of GPS in Smartphones 
and finds the route from their current location through Map Box’s API.

#Things you'd need.#
Dialog flow json file 
Google translator API (json file)
infermedica Api key (json file)
mapbox api key
newsApi key
