var mongoose = require('mongoose');

// create Stock schema
var StockSchema = new mongoose.Schema({
    Symbol:{
        type: String,
        required : true
    },
    Name:{
    	type: String,
    	required : true
    },
    Sector:{
    	type : String,
    	required : true
    },
    industry:{
    	type : String,
    	required : true
    },
    UserID:{
        type: String,
        required : true
    }
});

// Export the model schema
module.exports = StockSchema;
