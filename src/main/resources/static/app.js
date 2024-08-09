let ws, currentUser;

// On pressing Connect this method will be called
function connect() {
    ws = new WebSocket("ws://localhost:8080/hello");
    // This function will be called every time a new message arrives
    ws.onmessage = function (e) {
        console.log(e);
        printMessage(e.data);
    };

    document.getElementById("connectButton").disabled = true;
    document.getElementById("connectButton").value = "Connected";
    document.getElementById("name").disabled = true;
    currentUser = document.getElementById("name").value;
    console.log(currentUser);
    typingListening();
}

// This function takes care of printing the message on the browser
function printMessage(data) {
    let messages = document.getElementById("messages");
    let messageData = JSON.parse(data);
    let newMessage = document.createElement("div");
    newMessage.className = "incoming-message";
    newMessage.innerHTML = messageData.name + " : " + messageData.message;
    messages.appendChild(newMessage);
}

// This function handles functionality of sending the message to WebSocket
function sendToGroupChat() {
    if (!ws) return;

    let messageText = document.getElementById("message").value;
    document.getElementById("message").value = "";
    let messageObject = {
        name: currentUser,
        message: messageText,
    };

    let messages = document.getElementById("messages"); // Ensure this is declared
    let newMessage = document.createElement("div");
    newMessage.innerHTML = messageText + " : " + currentUser;
    newMessage.className = "outgoing-message";
    messages.appendChild(newMessage);

    // Send the message object
    ws.send(JSON.stringify(messageObject));

}

function typingListening() {
    const inputMessage = document.getElementById("message");
    let typingTimeout;

    inputMessage.addEventListener('input', function () {
        if (typingTimeout) {
            clearTimeout(typingTimeout);
        }

        ws.send(JSON.stringify({ type: 'typing', userId: currentUser }));

        typingTimeout = setTimeout(() => {
            ws.send(JSON.stringify({ type: 'stop_typing', userId: currentUser }));
        }, 1000);
    });

    ws.onmessage = (event) => {
        const data = JSON.parse(event.data);

        if (data.type === 'typing') {
            // Show typing indicator for the user
            showTypingIndicator(data.userId);
        } else if (data.type === 'stop_typing') {
            // Hide typing indicator for the user
            hideTypingIndicator(data.userId);
        } else {
            // Handle other message types
            printMessage(event.data); // Call printMessage for chat messages
        }
    };
}
console.log("is it Connected: "+cnt);

// Implement these functions to show/hide typing indicators
function showTypingIndicator(userId) {
    const outputMessage = document.getElementById("typing");
    outputMessage.textContent = `${userId} is typing...`;
}

function hideTypingIndicator(userId) {
    const outputMessage = document.getElementById("typing");
    outputMessage.textContent = ''; // Clear typing message
}
