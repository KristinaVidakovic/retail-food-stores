# Retail Food Store API

📗 [Swagger specification](http://localhost:8090/retail-food-stores/swagger-ui/index.html)

### 🗃️ List of implemented methods:

#### :department_store: Store Controller

| Method                    | Endpoint                      | Description                                                                                                                                                                                                                                                                                                                                                                                                                                  |
|---------------------------|-------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| :green_circle: *POST*     | */retail-food-stores/load*    | Method for loading data from provided csv file. <br> - HttpStatus **OK** <br> ➡️ Response : ***Response Message JSON object*** <br> 1️⃣ Successfully loaded data. <br> - HttpStatus **BAD REQUEST** <br> ➡️ Response : ***Unsupported File Exception JSON object*** <br> 1️⃣ Unsupported file provided. <br> - HttpStatus **EXPECTATION FAILED** <br> ➡️ Response : ***Unsupported File Exception JSON object*** <br> 1️⃣ Exception message. |
| :large_blue_circle: *GET* | */retail-food-stores/filter*  | Method for getting stores by provided filter. <br> - HttpStatus **OK** <br> ➡️ Response : ***List of Stores JSON objects with pagination*** <br> 1️⃣ Found stores. <br> - HttpStatus **NO CONTENT** <br> ➡️ Response : NO BODY                                                                                                                                                                                                               |
| 🔵 *GET*                  | */reatil-food-stores/nearest* | Method for getting n nearest stores <br> by provided longitude and latitude. <br> - HttpStatus **OK** <br> ➡️ Response : ***List of Stores JSON objects*** <br> 1️⃣ Successfully returned data by longitude and latitude.                                                                                                                                                                                                                    |
| :large_blue_circle: *GET* | */retail-food-stores/health*  | Method for checking status of the service.                                                                                                                                                                                                                                                                                                                                                                                                   |

#### :file_folder: Database used ➡️ **PostgreSQL**

####  Value for *DATABASE* variable is your database url 

**e.g.** *jdbc:postgresql://{hostname}:{port}/{db_name}?user={db_user}&password={db_pass}*

#### :hotsprings: Framework used ➡️ **Spring Boot**

#### :information_source: 

In root folder you can find postman collection with examples of API calls