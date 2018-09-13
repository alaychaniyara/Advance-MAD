const express= require ('express');
const router = express.Router();
const mongoose = require("mongoose");
const bcrypt = require("bcrypt");
const jwt= require('jsonwebtoken');
const checkauth=require('../authorisation-token/check-auth');

const Survey= require('../models/survey');
var token=null;
var newtoken=null;
//surveydata= new surveyData();
router.get("/", (req, res, next) => {
    return res.status(200).json({
        message: "Welcome to all surveys"
    });
});


//Insert Survey response to Database
router.post("/savesurvey", checkauth, (req, res, next) => {
    const newsurvey = new Survey({
        _id: new mongoose.Types.ObjectId(),
        survey_id:res.udata.email+(Math.random()*(Math.floor(99999)-Math.ceil(1)))+req.body.user_score,
        username:res.udata.email,
        response_1:req.body.response_1,
        response_2:req.body.response_2,
        response_3:req.body.response_3,
        response_4:req.body.response_4,
        response_5:req.body.response_5,
        response_6:req.body.response_6,
        response_7:req.body.response_7,
        response_8:req.body.response_8,
        response_9:req.body.response_9,
        response_10:req.body.response_10,
        user_score:req.body.user_score,
     });
    newsurvey
        .save()
        .then(result => {
            console.log(result);
            res.status(200).json({
                message: "Survey Responses Saved successfully",
              /*  createdProduct: {
                    name: result.name,
                    price: result.price,
                    _id: result._id,
                    request: {
                        type: 'GET',
                        url: "http://localhost:3000/products/" + result._id
                    }
                }*/
            });
        })
        .catch(err => {
            console.log(err);
            res.status(500).json({
                error: err
            });
        });
});

//View All Survey API
router.get("/allsurveys", (req, res, next) => {
    Survey.find()
        .exec()
        .then(docs => {
            const response = {
                count: docs.length,
                surveys: docs.map(doc => {
                    return {
                        username: doc.username,
                        survey_id: doc.survey_id,
                        response_1:doc.response_1,
                        response_2:doc.response_2,
                        response_3:doc.response_3,
                        response_4:doc.response_4,
                        response_5:doc.response_5,
                        response_6:doc.response_6,
                        response_7:doc.response_7,
                        response_8:doc.response_8,
                        response_9:doc.response_9,
                        response_10:doc.response_10,
                        user_score:doc.user_score
                    };
                })
            };
            if (docs.length <= 0) {
                res.status(404).json({
                    message: 'No entries found'
                });
            } else {
                //   if (docs.length >= 0) {
                res.status(200).json(response);
            }
        })

        .catch(err => {
            console.log(err);
            res.status(500).json({
                error: err
            });
        });
});
/*router.post("/savesurvey", (req, res, next) => {
    Survey.find({ email: req.body.email })
        .exec()
        .then(user => {
            if (user.length >= 1) {
                return res.status(409).json({
                    message: "E-mail already exists please try again with different email"
                });
            } else {
                bcrypt.hash(req.body.password, 10, (hasherror, hash) => {
                    if (hasherror) {
                        return res.status(500).json({
                            error: hasherror
                        });
                    } else {
                        user = new User({
                            _id: new mongoose.Types.ObjectId(),
                            email: req.body.email,
                            password: hash,
                            name:req.body.name,
                            age:req.body.age,
                            dateofbirth:req.body.dateofbirth,
                            address:req.body.address
                        });
                        user
                            .save()
                            .then(result => {

                                newtoken = jwt.sign(
                                    {
                                        userId: user._id,
                                        email: user.email,


                                    },
                                    process.env.JWT_KEY,
                                    {
                                        expiresIn: "1h"
                                    }
                                );
                                console.log(result);
                                res.status(200).json({
                                    message: "New User Succesfully Created",
                                    token:newtoken
                                });
                            })
                            .catch(err => {
                                console.log(err);
                                res.status(500).json({
                                    error: err
                                });
                            });
                    }
                });
            }
        });
});
*/
module.exports = router;
