// document.addEventListener("DOMContentLoaded", () => {
//     const taskInput = document.getElementById("task-input");
//     const addTaskButton = document.getElementById("add-task");
//     const taskList = document.getElementById("task-list");

//     const authToken = localStorage.getItem("idToken"); // Retrieve user ID from localStorage

//     if (!authToken) {
//         window.location.href = "/login"; // Redirect to login if user is not authenticated
//     }

//     // Fetch tasks when the page loads
//     fetchTasks();

//     // Add task event
//     addTaskButton.addEventListener("click", async () => {
//         const taskTitle = taskInput.value.trim();
//         if (taskTitle) {
//             await addTask(taskTitle);
//             taskInput.value = ""; // Clear the input field
//             fetchTasks(); // Reload tasks
//         }
//     });

//     // Fetch tasks from the backend
//     async function fetchTasks() {
//         const response = await fetch("/tasks", {
//             method: "GET",
//             headers: {
//                 "Content-Type": "application/json",
//                 "Authorization": `Bearer ${authToken}`, // Include token for authentication
//             }
//         });

//         const tasks = await response.json();
//         renderTasks(tasks);
//     }

//     // Render tasks in the UI
//     function renderTasks(tasks) {
//         taskList.innerHTML = ""; // Clear the task list
//         tasks.forEach(task => {
//             const taskElement = document.createElement("li");
//             taskElement.classList.add("mb-2", "flex", "items-center", "space-x-4");

//             const taskTitle = document.createElement("span");
//             taskTitle.textContent = task.title;
//             taskTitle.classList.add(task.completed ? "line-through" : "a");

//             const editButton = document.createElement("button");
//             editButton.textContent = "Edit";
//             editButton.classList.add("bg-yellow-500", "text-white", "px-2", "py-1", "rounded");
//             editButton.addEventListener("click", () => editTask(task.id));

//             const deleteButton = document.createElement("button");
//             deleteButton.textContent = "Delete";
//             deleteButton.classList.add("bg-red-500", "text-white", "px-2", "py-1", "rounded");
//             deleteButton.addEventListener("click", () => deleteTask(task.id));

//             taskElement.appendChild(taskTitle);
//             taskElement.appendChild(editButton);
//             taskElement.appendChild(deleteButton);

//             taskList.appendChild(taskElement);
//         });
//     }

//     // Add new task
//     async function addTask(title) {
//         const response = await fetch("/tasks", {
//             method: "POST",
//             headers: {
//                 "Content-Type": "application/json",
//                 "Authorization": `Bearer ${authToken}`,
//             },
//             body: JSON.stringify({ title })
//         });

//         const result = await response.json();
//         if (result.success) {
//             fetchTasks();
//         } else {
//             alert("Failed to add task");
//         }
//     }

//     // Edit a task
//     async function editTask(taskId) {
//         const newTitle = prompt("Edit task title:");
//         if (newTitle) {
//             const response = await fetch(`/tasks/${taskId}`, {
//                 method: "PUT",
//                 headers: {
//                     "Content-Type": "application/json",
//                     "Authorization": `Bearer ${authToken}`,
//                 },
//                 body: JSON.stringify({ title: newTitle })
//             });

//             const result = await response.json();
//             if (result.success) {
//                 fetchTasks();
//             } else {
//                 alert("Failed to edit task");
//             }
//         }
//     }

//     // Delete a task
//     async function deleteTask(taskId) {
//         const response = await fetch(`/tasks/${taskId}`, {
//             method: "DELETE",
//             headers: {
//                 "Authorization": `Bearer ${authToken}`,
//             }
//         });

//         const result = await response.json();
//         if (result.success) {
//             fetchTasks();
//         } else {
//             alert("Failed to delete task");
//         }
//     }
// });
