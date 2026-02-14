console.log("This is Contact.js");

const baseUrl = "http://localhost:8080";

document.addEventListener("DOMContentLoaded", () => {

    const viewContactModal = document.getElementById("view_contact_modal");

    const contactModal = new Modal(viewContactModal, {
        closable: true
    });

    window.openContactModal = function () {
        console.log("OPEN MODAL");
        contactModal.show();
    };

    window.closeContactModal = function () {
        console.log("Close Modal")
        contactModal.hide();
    }

    window.loadContactModal = async function  (id) {
        console.log(id)
        try{
          const data = await (await fetch(`${baseUrl}/api/contacts/${id}`)).json();
          console.log(data);
          document.querySelector("#contact_name").innerHTML = data.name;
          document.querySelector("#contact_email").innerHTML = data.email;
          document.querySelector("#contact_picture").src = data.picture;
          document.querySelector("#contact_number").innerHTML = data.phoneNumber;
          document.querySelector("#contact_address").innerHTML = data.address;
          document.querySelector("#contact_description").innerHTML = data.description;
          document.querySelector("#contact_facebook").innerHTML = data.facebookLink;
          document.querySelector("#contact_linkedin").innerHTML = data.linkedinLink;
          openContactModal()
        }catch(error){
           console.error("Error loading contact : ",error);
        }

    }

});


async function deleteContact(id) {
     Swal.fire({
       title: "Do you want Delete the Contact?",
       icon : "warning",
       showCancelButton: true,
       confirmButtonText: "Delete",
     }).then((result) => {
       if (result.isConfirmed) {
           const url = `${baseUrl}/user/contacts/delete/`+id;
           window.location.replace(url);
       }
     });
}