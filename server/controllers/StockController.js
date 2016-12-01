var restful = require('node-restful'); //convert mongo object to rest-api

module.exports = function(app,route){
    //Setup for RESt.
    var rest =restful.model(
    'stock',
    app.models.stock
    ).methods(['get','put','post','delete']);
    // Register this end with the app
    rest.register(app, route);
    
    //Return middleware
    return function(req,res,next){
        next();
    };
};
