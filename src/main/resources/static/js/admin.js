console.log("Hello Admin");

document.querySelector("#input_file").addEventListener('change',function(event){

     let file = event.target.files[0];
     let reader = new FileReader();
     reader.onload = function(){
          document.getElementById("upload_image_preview").src=reader.result;
     };
     reader.readAsDataURL(file);

})