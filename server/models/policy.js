var mongoose = require('mongoose');

// create Stock schema
var PolicySchema = new mongoose.Schema({
    ApplicationID: String,
    ApplicationType: String,
    Latitude: Number,
    Longtitude: Number,
    Networks: [{
        NetworkSSID: String,
        Bandwidth: Number,
        SignalStrength: Number,
        SignalFrequency: Number,
        TimeToConnect: Number,
        Cost: Number,
        SecurityProtocol: String
    }]
})

// Export the model schema
module.exports = PolicySchema;
