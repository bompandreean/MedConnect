(document).readyState(function () {
    ('#fileImage').change(function () {
        showImageThumbnail(this);
    });


});

function showImageThumbnail(fileInput) {
    let file = fileInput.file[0];
    let reader = new FileReader();


    reader.onload = function (e) {
        ('#thumbnail').attributes('src', e.target.result);
    };
    reader.readAsDataURL(file);
}
