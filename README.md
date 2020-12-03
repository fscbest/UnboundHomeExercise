# UnboundHomeExercise
Home assignment to implement REST API using a JAVA service, that provide  cryptographic sign functionalities.

# To run:
1. Download HomeExercise-0.0.1-SNAPSHOT.jar.zip
2. Unzip HomeExercise-0.0.1-SNAPSHOT.jar.zip
3. Execute from terminal: java -jar HomeExercise-0.0.1-SNAPSHOT.jar

# Swagger:
http://localhost:8080/swagger-ui.html

# Requests to test:
1. Generate new RSA key pair:
   curl --location --request POST 'localhost:8080/key/generate'
   
2. Delete an existing RSA key pair:
   curl --location --request DELETE 'localhost:8080/key/deletebyid/dbdd1e92-46a2-41a4-9c34-4fd9eec190cb' \
--data-raw ''

3. Sign on a given data using the given key:
   curl --location --request POST 'localhost:8080/key/sign/51446e64-7407-4535-ab4c-df8d47c40ba1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "data":"Test DATA for signing."
}'

4. Verify the given signature on the given data using the given key:
  curl --location --request GET 'localhost:8080/key/sign/verify/51446e64-7407-4535-ab4c-df8d47c40ba1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "signature":"DvyyqQSVGfONmlWg2x/QR6CBqBLHvV1rxH14Dl00BNzVSS5fgYx2EeXA4b4VSCFPJu4Clw9Tvr5RuEcb8FfQOODne83tG+2pWOEUpx3l4N+a9R4PTSgtEKg4bBrQsxZyFguQ9EocKOFCnMDITdHeMROmTPVQaR9K4TwClqALBTxJ5VJEio5jh1fBG5YgcWFUdOsgeI7tUa2/Vp24JCnItnw/QTHz9mYhUyIIfTN3Ptxj1InpHPMLPR5HFjHr1ZpjQWyRDNQ8tFdbzwf74nYpMlrfkvEgbud4N/x8KM65DpRYdt67t1Ipf/9KfUxnTpyeqV17w/Zu8sBqazDZP/sraw==",
    "data":"Test DATA for signing."
    
}'

5.List all existing keys IDs:
  curl --location --request GET 'localhost:8080/key/list'
  
