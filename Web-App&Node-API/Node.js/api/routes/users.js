const express= require ('express');
const router = express.Router();
const mongoose = require("mongoose");
const bcrypt = require("bcrypt");
const jwt= require('jsonwebtoken');
const checkauth=require('../authorisation-token/check-auth');

const User= require('../models/user');
var token=null;
var newtoken=null;
userdata= new User();
    router.get("/", (req, res, next) => {
    return res.status(200).json({
        message: "Welcome"
    });
});

//SIGNUP API

router.post("/signup", (req, res, next) => {
    User.find({ email: req.body.email })
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

//LOGIN API
router.post("/login", (req, res, next) => {
    User.find({ email: req.body.email })
        .exec()
        .then(user => {
            if (user.length < 1) {
                return res.status(401).json({
                    message: " No User with email" +req.body.email+" found."
                });
            }

            bcrypt.compare(req.body.password, user[0].password, (err, result) => {
                if (err) {
                    return res.status(401).json({
                        message: "Hash Compare failed"
                    });
                }
                if (result) {

                    token = jwt.sign(
                        {
                            userId: user[0]._id,
                            email: user[0].email,

                        },
                        process.env.JWT_KEY,
                        {
                            expiresIn: "1h"
                        }
                    );
                    return res.status(200).json({
                        message: "You have successfully Logged In System",
                        token: token

                    });
                }
                res.status(401).json({
                    message: 'Incorrect Password'
                });
            });
        })
        .catch(err => {
            console.log(err);
            res.status(500).json({
                error: err
            });
        });
});
//To get User Information from API
/*
router.get('/userinfo', function(req, res) {
    //var token = req.headers['x-access-token'];

    const token = req.headers.authorization.split(" ")[1];
    if (!token) return res.status(401).send({ auth: false, message: 'No token provided.' });

    jwt.verify(token, config.secret, function(err, decoded) {
        if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });

        res.status(200).send(decoded);
    });
});
*/
router.get("/userinfo",checkauth,(req, res, next) => {
    User.findById(res.udata.userId)
        .exec()
        .then(user => {
            if (!user) {
                return res.status(404).json({
                    message: "User Data not found"
                });
            }
            res.status(200).json({
                user: user,
              /*  request: {
                    type: "GET",
                    url: "http://localhost:3000/users/"
                }*/
            });
        })
        .catch(err => {
            res.status(500).json({
                error: err
            });
        });
});
//To Update the User Info
router.patch("/updateuser",checkauth, (req, res, next) => {
    console.log(req);
    const id = res.udata.userId;
    const updateOps = {};
    for (const ops of req.body) {
        updateOps[ops.propName] = ops.value;
    }
    User.update({ _id: id }, { $set: updateOps })
        .exec()
        .then(result => {
            res.status(200).json({
                message: 'User information updated',
             /*   request: {
                    type: 'GET',
                    url: 'http://localhost:3000/products/' + id
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
//To Delete a specific User from Database

router.delete("/:userId", (req, res, next) => {
    User.remove({ _id: req.params.userId })
        .exec()
        .then(result => {
            res.status(200).json({
                message: "User is deleted"
            });
        })
        .catch(err => {
            console.log(err);
            res.status(500).json({
                error: err
            });
        });
});

//To View List of All Users and their info
router.get("/allusers", (req, res, next) => {
    User.find()
        .select("email name _id age address dateofbirth")
        .exec()
        .then(docs => {
            const response = {
                count: docs.length,
                users: docs.map(doc => {
                    return {
                        email: doc.email,
                        name: doc.name,
                        _id: doc._id,
                        age: doc.age,
                        address: doc.address,
                        dateofbirth: doc.dateofbirth
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
module.exports = router;


//update
