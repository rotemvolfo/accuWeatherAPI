  Code structure, design patterns & readability
  
I Implemented a wrapper class (AdapterAccuWeather ) as an adapter for the calls to the AccuWeather API. 
The adapter class creates an extra layer between the 3rd party and the app that allows extending the code and making future changes without changing the existing code.
In addition, I used the Factory design pattern in order to simplify the adding of a new service provider in the future and to easily switch between the providers.

Alerts 

The app will send alerts to Big Pand about the current weather in San Francisco 
In addition, it sends alerts about failed API calls to AccuWeather.
Because the integration with the 3rd party is the main functionality of the app we want to know if the 3rd party is down. 

Tests

I created a functionality test for the function responsible to create a JSON in BigPanda format. 
I used Mockito to mock (Java library for testing ) the API calls. 
In an app that uses a 3rd party as core functionality, monitoring the 3rd party as an integral part of the product infrastructure is crucial. 
With that being said, in the case of a down 3rd party server, we are sending an alert to BigPanda to inform about this incident. 
  

Correlation : 

Added Correlation based on zip code as mention in the exercise. 

Enrichment : 

I used the enrichments to add additional information about the snow and rain tags and added special instructions based on their values.
   
Additional notes. 

I wasn’t able to retrieve the wether condition of neighboring cities of San Francisco.
The “city neighbors API”  call return locations inside the city or really close to the city so I skipped this functionality. 
The code supports only the returning weather conditions of San Francisco.
I thought about looking for neighboring cities weather the same as I did for San Francisco, but I didn’t want to do lots of API calls and adding a code that does more of the same. Instead, I  focused more on the design, tests and working with big panda interface.
