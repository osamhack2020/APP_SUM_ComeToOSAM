const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

function getRandomInt(min, max) { //min ~ max 사이의 임의의 정수 반환
    return Math.floor(Math.random() * (max - min)) + min;
}

exports.addIndexIntimacy = functions.https.onCall((data) => {
    const uid = data.uid;
    
    return admin.database().ref('IndexIntimacy/' + uid).once('value').then(snap => {
        if (!snap.exists()) {
            const a1 = getRandomInt(1, 20);
            const a2 = getRandomInt(1, 20);
            const a3 = getRandomInt(1, 20);
            const a4 = getRandomInt(1, 20);
            const a5 = getRandomInt(1, 20);
            const a6 = getRandomInt(1, 20);
            const a7 = getRandomInt(1, 20);
            const a8 = getRandomInt(1, 20);
            const a9 = getRandomInt(1, 20);
            const a10 = getRandomInt(1, 20);

            return admin.database().ref('IndexIntimacy/' + uid).set({
                "id" : uid,
                "position1" : String(a1),
                "position2" : String(a2),
                "milEdu1" : String(a3),
                "milEdu2" : String(a4), 
                "priEdu1" : String(a5), 
                "priEdu2" : String(a6),
                "milCareer1" : String(a7),
                "milCareer2" : String(a8),
                "privacy1" : String(a9), 
                "privacy2" : String(a10)
            });
        } else {
            return null;
        }
    });
});

exports.addIndexExpert = functions.https.onCall((data) => {
    const uid = data.uid;

    return admin.database().ref('IndexExpert/'+uid).once('value').then(snap => {
        if (!snap.exists()) {
            const a1 = getRandomInt(0, 25);
            const a2 = getRandomInt(0, 25);
            const a3 = getRandomInt(0, 25);
            const a4 = getRandomInt(0, 25);
            const a5 = getRandomInt(0, 25);
            const a6 = getRandomInt(0, 25);
            const a7 = getRandomInt(0, 25);
            const a8 = getRandomInt(0, 25);
            const a9 = getRandomInt(0, 25);
            const a10 = getRandomInt(0, 25);
            const a11 = getRandomInt(0, 25);
            const a12 = getRandomInt(0, 25);
            const a13 = getRandomInt(0, 25);
            const a14 = getRandomInt(0, 25);
            const a15 = getRandomInt(0, 25);
            const a16 = getRandomInt(0, 25);
            const a17 = getRandomInt(0, 25);
            const a18 = getRandomInt(0, 25);
            const a19 = getRandomInt(0, 25);
            const a20 = getRandomInt(0, 25);

            return admin.database().ref('IndexExpert/'+uid).set({
                "id" : uid,
                "language_position" : String(a1),
                "language_edu" : String(a2),
                "language_career" : String(a3),
                "language_performance" : String(a4), 
                "combat_position" : String(a5),
                "combat_edu" : String(a6),
                "combat_career" : String(a7),
                "combat_performance" : String(a8), 
                "computer_position" : String(a9),
                "computer_edu" : String(a10),
                "computer_career" : String(a11),
                "computer_performance" : String(a12), 
                "admin_position" : String(a13),
                "admin_edu" : String(a14),
                "admin_career" : String(a15),
                "admin_performance" : String(a16), 
                "law_position" : String(a17),
                "law_edu" : String(a18),
                "law_career" : String(a19),
                "law_performance" : String(a20)
            });
        } else {
            return null;
        }
    });
});

exports.addIndexExpert = functions.https.onCall((data) => {
    const uid = data.uid;

    return admin.database().ref('IndexIntimacy').then(snap => {
        if (!snap.exists()) {
            const a1 = getRandomInt(0, 25);
            const a2 = getRandomInt(0, 25);
            const a3 = getRandomInt(0, 25);
            const a4 = getRandomInt(0, 25);
            const a5 = getRandomInt(0, 25);
            const a6 = getRandomInt(0, 25);
            const a7 = getRandomInt(0, 25);
            const a8 = getRandomInt(0, 25);
            const a9 = getRandomInt(0, 25);
            const a10 = getRandomInt(0, 25);
            const a11 = getRandomInt(0, 25);
            const a12 = getRandomInt(0, 25);
            const a13 = getRandomInt(0, 25);
            const a14 = getRandomInt(0, 25);
            const a15 = getRandomInt(0, 25);
            const a16 = getRandomInt(0, 25);
            const a17 = getRandomInt(0, 25);
            const a18 = getRandomInt(0, 25);
            const a19 = getRandomInt(0, 25);
            const a20 = getRandomInt(0, 25);

            return admin.database().ref('IndexExpert/'+uid).set({
                "id" : uid,
                "language_position" : String(a1),
                "language_edu" : String(a2),
                "language_career" : String(a3),
                "language_performance" : String(a4), 
                "combat_position" : String(a5),
                "combat_edu" : String(a6),
                "combat_career" : String(a7),
                "combat_performance" : String(a8), 
                "computer_position" : String(a9),
                "computer_edu" : String(a10),
                "computer_career" : String(a11),
                "computer_performance" : String(a12), 
                "admin_position" : String(a13),
                "admin_edu" : String(a14),
                "admin_career" : String(a15),
                "admin_performance" : String(a16), 
                "law_position" : String(a17),
                "law_edu" : String(a18),
                "law_career" : String(a19),
                "law_performance" : String(a20)
            });
        } else {
            return null;
        }
    });
});
    
exports.addIndexExpert = functions.https.onCall((data) => {
    const uid = data.uid;

});
    
