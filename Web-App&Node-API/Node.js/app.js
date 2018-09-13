const express= require ('express');
const app = express();
const morgan= require('morgan');
const bodyParser = require("body-parser");
const mongoose = require("mongoose");
const userRoutes= require('./api/routes/users');
const surveyRoutes= require('./api/routes/survey');
try{
 mongoose.connect(
     'mongodb+srv://amad-user:'+process.env.MONGO_ATLAS_PW+'@advance-mad-6lt06.mongodb.net/test?retryWrites=true',
     {
         useNewUrlParser: true
     }
 );}catch (error) {
    return res.status(401).json({
        message: 'Server COnnect Fail'
    });
}
 mongoose.Promise = global.Promise;


 app.use(morgan('dev'));
 app.use(bodyParser.urlencoded({ extended: false }));
 app.use(bodyParser.json());
//CORS Handling
 app.use((req, res, next) => {
     res.header("Access-Control-Allow-Origin", "*");
     res.header(
         "Access-Control-Allow-Headers",
         "Origin, X-Requested-With, Content-Type, Accept, Authorization"
     );
     if (req.method === "OPTIONS") {
         res.header("Access-Control-Allow-Methods", "PUT, POST, PATCH, DELETE, GET");
         return res.status(200).json({});
     }
     next();
 });
 //Handling User Routes
app.use('/users',userRoutes);

app.use('/survey',surveyRoutes);



//ERROR HANDLING
app.use((req,res,next) => {
    const error= new Error('NOT FOUND');
    error.status(404);
    next(error);
});

 app.use((error, req, res, next) => {
     res.status(error.status || 500);
     res.json({
         error: {
             message: error.message
         }
     });
 });
module.exports = app;