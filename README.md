How to run?
- start the project locally and go to localhost:8080 (Swagger will open)
- call '/report' to see what's in DB 

Notes/comments:
- there is no proper rest error handling
- the api and impl layers are not separated
- there is no transaction support
- it's better to use existing model mappers (e.g. Dozer)
- data initialization in test don't seem well
- there are still some magic numbers and unclear logic in service, I would prefer to come up with some ratio or something, but.. better done, than perfect :)  



