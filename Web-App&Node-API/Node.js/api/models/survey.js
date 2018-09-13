const  mongoose= require('mongoose');

const  surveySchema= mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    survey_id:{
        type:String,
        required:true,
        unique:true,
    },
    username: {
        type: String,
        required: true,
        match: /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/
    },
    response_1:{type: String},
    response_2:{type: String},
    response_3:{type: String},
    response_4:{type: String},
    response_5:{type: String},
    response_6:{type: String},
    response_7:{type: String},
    response_8:{type: String},
    response_9:{type: String},
    response_10:{type: String},
    user_score:{type:String},

});

module.exports=mongoose.model('Survey',surveySchema);