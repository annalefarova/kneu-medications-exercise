#### Assumptions that have been made:

- When creating prescription user would have to peek a medication from list of all medications to include a medication 
id in `CreatePrescriptionRequest` 
- The real medication database would contain more than just five medications. That is why pagination was added to 
list query.
- The intake time input format is currently restricted to `"HH:mm"`. The possible intake time formats that need to be
supported need to be clarified and then model and validator would require changes.
- Since no additional requirements were specified added a creation timestamp for prescription to be able to differentiate 
when one was created.


#### Remaining things to do

- Extend tests to cover pagination. Create a medications test data as json file and preload repository before testing.
- Improve tests by removing hardcoded test data, create and load test data from json file instead.
- Check No static resource metrics exception in logs.


#### Possible extensions

- Currently prescription has only one intake time which would require creating two prescriptions for same medication if 
this one should be taken twice a day. Prescription model could be extended to have a set of intake times and  
possibly start and end date. Set of intake times could be stored as a PostgreSql array instead of `@ElemenCollection`
to avoid join operations when querying. That would also require changing test setup to use test containers with 
PostgreSql since specific PostgreSql arrays can not be handled by in memory H2 database.

- Possibly duplicated prescriptions should be covered. With current simple model and added creation timestamp possible
behavior could be: check existing prescription for same date and intake time before creating a new one. 