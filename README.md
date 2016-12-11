# Covered
Android Application that allows you to set Alarms to remind you to take the right things (Umbrella, Coat, Scarf, Gloves, etc) before you leaving, according to the Weather outside.

Technical Requirements (5 Points)
# Demonstrate your proficiency in mobile user interface
  * Use model-view-controller model for user interface in your app (IMPLEMENTED)
  * Implement user interface layout design and handle event messaging (IMPLEMENTED)
  * Show the changing UI in response to orientation sensor (portrait mode and landscape mode) (IMPLEMENTED)
    -> SignedIn_Landscape_Fragment.
    -> SignedIn_Portrait_Fragment.
    
# Show your skills in working with mobile device features
  * Demonstrate the use of touch and gesture (IMPLEMENTED)
    -> Use of Long Click and Other touch gestures on Form fields and Buttons.
  * Work with at least one kind of sensor technologies, such as accelerometer, gyroscope, compass, etc. (IMPLEMENTED)
    -> Implemented Compass on the Sign In Page on the Application.
  * Use one of the location services: geo-location, geo-coding, etc. (IMPLEMENTED)
    -> Used for finding the current location of the user in SignedIn_Landscape_Fragment.
    
# Show your code for data storage
  * Add application state persistence to your app (IMPLEMENTED)
    -> Used Database to store and Retrieve data for multiple usees in the application.
    -> Used SharedPreferences to store and retrieve selected background picture for the application's. SignedIn_Portrait_Fragment page
  * Demonstrate the read/write operations for file storage (PARTIALLY IMPLEMENTED)
    -> Implemented Read Photo from Gallery.
  * Handle database operations: create/read/update/delete (IMPLEMENTED)
  
# Work with web services
  * Enable single sign-on authentication service using either Google or Facebook sign on service (IMPLEMENTED)
  * Use at least one web service such as weather report, real-time stock quote, current traffic info, etc. (IMPLEMENTED)
    -> Used OpenWeatherMap API for getting Real time Weather Information.
    
