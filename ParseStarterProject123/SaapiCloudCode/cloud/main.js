
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.afterSave("Message", function(request) {
	
	if(request.object.existed() == true) {
		return;
	}
	
	var pushQuery = new Parse.Query(Parse.Installation);
	pushQuery.equalTo("username", request.object.get("userID"));
	//var pushQuery = new Parse.Query(Parse.Installation);

	Parse.Push.send({
        where: pushQuery,
        data: {
            alert: "You Have A New Message",
            sound: "default"
              }
        },{
        success: function(){
           response.success('true');
        },
        error: function (error) {
           response.error(error);
        }
    });
});