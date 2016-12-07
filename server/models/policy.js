var mongoose = require('mongoose');

// create Stock schema
var PolicySchema = new mongoose.Schema({
    applicationId:{
        type: String,
        required : false
    },
    Location:{
        type: String,
        required : false
    },
    networkSSID:{
        type: String,
        required : false
    },
    bandwidth:{
        type: Number,
        required: false
    },
    signalStrength:{
        type: Number,
        required: false
    },
    signalFrequency:{
        type: Number,
        required: false
    },
    timeToConnect:{
        type: Number,
        required:false
    },
    cost:{
        type: Number,
        required:false
    }
})

// Export the model schema
module.exports = PolicySchema;
