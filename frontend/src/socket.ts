import {reactive} from "vue";
import {io} from "socket.io-client";

export const state = reactive({
  connected: false,
  fooEvents: [],
  barEvents: []
});
const host = window.location.hostname;
const URL = `https://${host}:443`;
console.log("Connecting to websocket server at", URL);

export const socket = io(URL, {
    path: "/ws", withCredentials: true, secure: true
  }
);

socket.on("connect", () => {
  state.connected = true;
  console.log("Connected to websocket server");
});

socket.on("disconnect", () => {
  state.connected = false;
  console.log("Disconnected from websocket server");
});
