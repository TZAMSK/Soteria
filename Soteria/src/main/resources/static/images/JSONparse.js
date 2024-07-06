function parseProfileData(){
    var profileData = document.getElementById("profileData").textContent;
    var profileJson = JSON.parse(profileData);

    var displayName = profileJson.Response.profile.data.userInfo.displayName;

    document.getElementById("displayName").innerText = displayName;
}