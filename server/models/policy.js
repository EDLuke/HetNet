var mongoose = require('mongoose');

// create Stock schema
var PolicySchema = new mongoose.Schema({
    name:{
        type: String,
        required : true
    },
    password:{
        type: String,
        required : true
    },
    email:{
        type: String,
        required : true
    }
})

// Export the model schema
module.exports = PolicySchema;
