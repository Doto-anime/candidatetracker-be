# Candidate Tracker Backend
### Postman Collection

import postman collections from Candidate Tracker.postman_collection.json file.

### Testing backend :
For testing purpose, the backend can be access here: https://candidatetracker-be.herokuapp.com/

example request: https://candidatetracker-be.herokuapp.com/api/auth/signin

It's a free hosting. The service is unavailable at 12 am to 8 am and might be slow

### Available working endpoind (see postman collection) :
- Sign in = /api/auth/signin POST
- Sign up = /api/auth/signup POST
- Create job = /api/jobs POST
- Update job (update job status or other field) = /api/jobs/:id PATCH
- Get job by id = /api/jobs/:id GET
- Get list of my jobs = /api/users/jobs GET
- Check username availability = /api/users/checkUsername?username=[username] GET
- User profile = /api/users/profile GET
- Add stage (with note) to certain job = /api/jobs/:id/stages POST
- Update stage of a certain job = /api/jobs/:jobId/stages/:stageId PATCH

