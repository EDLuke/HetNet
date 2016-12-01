var express = require('express');
var mongoose = require('mongoose');
var bodyParser = require('body-parser');
var methodOverrde = require('method-override');
var helmet = require('helmet');
var _ = require('lodash');

//creating express app
var app = express(); 

//Add Middleware for REST API
//app.use(bodyParser.urlencoded({extended:true}));
app.use(bodyParser.json());
//app.use(methodOverrde('X-HTTP-Method-Override'));

//Add CORS 
app.use(function(req,res,next){
    res.header('Access-Control-Allow-Origin','*');
    res.header('Access-Control-Allow-Methods','GET,PUT,POST,DELETE');
    res.header('Access-Control-Allow-Headers','Content-Type');
    next();
});

// Add security
app.use(helmet());
app.use(helmet.noCache());

app.use('/hello',function(req, res, next){
    res.send('Hello World');
    next();
});


//Connect to MongoDB
mongoose.connect('mongodb://localhost/Hetnet');
mongoose.connection.once('open', function(){
    //load models
    app.models = require('./models/index');
    //Load Routes
    var routes = require('./routes')
    _.each(routes, function(controller, route){
           app.use(route, controller(app, route));
    });
    console.log('Listening on port 8008...')
    app.listen(8008);
});

