const API_BASE = 'http://localhost:8080/api/tasks';

let tasks = [];
let editingTaskId = null;

const taskList = document.getElementById('task-list');
const statusFilter = document.getElementById('status-filter');
const priorityFilter = document.getElementById('priority-filter');
const newTaskButton = document.getElementById('new-task-button');
const taskDialogBackdrop = document.getElementById('task-dialog-backdrop');
const taskDialog = document.getElementById('task-dialog');
const taskForm = document.getElementById('task-form');
const dialogTitle = document.getElementById('dialog-title');
const taskTitleInput = document.getElementById('task-title');
const taskDescriptionInput = document.getElementById('task-description');
const taskPriorityInput = document.getElementById('task-priority');
const taskDueDateInput = document.getElementById('task-duedate');
const taskCancelButton = document.getElementById('task-cancel-button');
const confirmBackdrop = document.getElementById('confirm-dialog');
const confirmDeleteButton = document.getElementById('confirm-delete-button');
const confirmCancelButton = document.getElementById('confirm-cancel-button');

async function fetchTasks() {
  try {
    const response = await fetch(API_BASE);
    if (!response.ok) {
      throw new Error(`Failed to fetch tasks: ${response.status} ${response.statusText}`);
    }
    tasks = await response.json();
    renderTasks();
  } catch (error) {
    alert(error.message);
  }
}

function renderTasks() {
  const selectedStatus = statusFilter.value;
  const selectedPriority = priorityFilter.value;

  const filteredTasks = tasks.filter((task) => {
    const statusMatches = selectedStatus === 'ALL' || task.status === selectedStatus;
    const priorityMatches = selectedPriority === 'ALL' || task.priority === selectedPriority;
    return statusMatches && priorityMatches;
  });

  if (!filteredTasks.length) {
    taskList.innerHTML = '<div style="grid-column:1/-1; padding: 2rem; background: #fff; border-radius: 1rem; text-align: center; box-shadow: 0 12px 24px rgba(15,23,42,0.08);">No tasks found</div>';
    return;
  }

  taskList.innerHTML = filteredTasks.map((task) => {
    const dueDateMarkup = task.dueDate ? `<div class="warning-pill">${isPastDue(task.dueDate) ? '⚠️ ' : ''}${formatDate(task.dueDate)}</div>` : '';
    const statusClass = getStatusBadgeClass(task.status);
    const priorityClass = getPriorityBadgeClass(task.priority);

    return `
      <article class="task-card ${isPastDue(task.dueDate) ? 'warning' : ''}">
        <div>
          <h3 class="task-title">${escapeHtml(task.title)}</h3>
          <div class="task-meta">
            <span class="badge ${statusClass}">${escapeHtml(task.status)}</span>
            <span class="badge ${priorityClass}">${escapeHtml(task.priority)}</span>
          </div>
        </div>
        <div class="task-body">${task.description ? escapeHtml(task.description) : 'No description provided.'}</div>
        ${dueDateMarkup}
        <div class="task-footer">
          <div class="task-actions">
            <button type="button" data-action="edit" data-id="${task.id}">Edit</button>
            <button type="button" data-action="delete" data-id="${task.id}">Delete</button>
          </div>
          <label>
            <span style="display:none">Status</span>
            <select data-action="status" data-id="${task.id}">
              ${renderStatusOption('TODO', task.status)}
              ${renderStatusOption('IN_PROGRESS', task.status)}
              ${renderStatusOption('COMPLETE', task.status)}
            </select>
          </label>
        </div>
      </article>
    `;
  }).join('');

  Array.from(taskList.querySelectorAll('[data-action]')).forEach((element) => {
    const action = element.getAttribute('data-action');
    const id = element.getAttribute('data-id');

    if (action === 'edit') {
      element.addEventListener('click', () => {
        const task = tasks.find((item) => String(item.id) === id);
        if (task) {
          openModal(task);
        }
      });
    }

    if (action === 'delete') {
      element.addEventListener('click', async () => {
        await deleteTask(id);
      });
    }

    if (action === 'status') {
      element.addEventListener('change', async (event) => {
        await changeStatus(id, event.target.value);
      });
    }
  });
}

async function createTask(formData) {
  try {
    const response = await fetch(API_BASE, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(formData),
    });

    if (!response.ok) {
      const errorText = await getErrorText(response);
      throw new Error(errorText);
    }

    await fetchTasks();
    closeModal();
  } catch (error) {
    alert(error.message);
  }
}

async function updateTask(id, formData) {
  try {
    const response = await fetch(`${API_BASE}/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(formData),
    });

    if (!response.ok) {
      const errorText = await getErrorText(response);
      throw new Error(errorText);
    }

    await fetchTasks();
    closeModal();
  } catch (error) {
    alert(error.message);
  }
}

async function changeStatus(id, newStatus) {
  try {
    const response = await fetch(`${API_BASE}/${id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: newStatus }),
    });

    if (!response.ok) {
      const errorText = await getErrorText(response);
      throw new Error(errorText);
    }

    await fetchTasks();
  } catch (error) {
    alert(error.message);
  }
}

async function deleteTask(id) {
  const confirmed = await showConfirmDialog();
  if (!confirmed) {
    return;
  }

  try {
    const response = await fetch(`${API_BASE}/${id}`, {
      method: 'DELETE',
    });

    if (!response.ok) {
      const errorText = await getErrorText(response);
      throw new Error(errorText);
    }

    await fetchTasks();
  } catch (error) {
    alert(error.message);
  }
}

function openModal(task = null) {
  taskForm.reset();
  editingTaskId = null;

  if (task) {
    dialogTitle.textContent = 'Edit Task';
    taskTitleInput.value = task.title || '';
    taskDescriptionInput.value = task.description || '';
    taskPriorityInput.value = task.priority || 'MEDIUM';
    taskDueDateInput.value = task.dueDate || '';
    editingTaskId = task.id;
  } else {
    dialogTitle.textContent = 'Create Task';
    taskPriorityInput.value = 'MEDIUM';
  }

  taskDialogBackdrop.style.display = 'flex';
  if (typeof taskDialog.showModal === 'function') {
    taskDialog.showModal();
  }
}

function closeModal() {
  taskForm.reset();
  editingTaskId = null;
  if (typeof taskDialog.close === 'function') {
    taskDialog.close();
  }
  taskDialogBackdrop.style.display = 'none';
}

async function handleFormSubmit(event) {
  event.preventDefault();

  const title = taskTitleInput.value.trim();
  const description = taskDescriptionInput.value.trim();
  const priority = taskPriorityInput.value;
  const dueDate = taskDueDateInput.value;

  const formData = {
    title,
    priority,
  };

  if (description) {
    formData.description = description;
  }

  if (dueDate) {
    formData.dueDate = dueDate;
  }

  if (editingTaskId) {
    await updateTask(editingTaskId, formData);
  } else {
    await createTask(formData);
  }
}

function getStatusBadgeClass(status) {
  switch (status) {
    case 'TODO':
      return 'badge-status-todo';
    case 'IN_PROGRESS':
      return 'badge-status-in_progress';
    case 'COMPLETE':
      return 'badge-status-complete';
    default:
      return 'badge-status-todo';
  }
}

function getPriorityBadgeClass(priority) {
  switch (priority) {
    case 'LOW':
      return 'badge-priority-low';
    case 'MEDIUM':
      return 'badge-priority-medium';
    case 'HIGH':
      return 'badge-priority-high';
    default:
      return 'badge-priority-medium';
  }
}

function formatDate(dateString) {
  if (!dateString) {
    return '';
  }
  const date = new Date(dateString);
  return date.toLocaleDateString(undefined, {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  });
}

function isPastDue(dateString) {
  if (!dateString) {
    return false;
  }
  const taskDate = new Date(dateString);
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  return taskDate < today;
}

function renderStatusOption(value, currentStatus) {
  return `<option value="${value}" ${value === currentStatus ? 'selected' : ''}>${value}</option>`;
}

function escapeHtml(value) {
  return String(value)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;');
}

async function getErrorText(response) {
  try {
    const data = await response.json();
    return data.message || JSON.stringify(data);
  } catch (_) {
    return `${response.status} ${response.statusText}`;
  }
}

function showConfirmDialog() {
  return new Promise((resolve) => {
    confirmBackdrop.style.display = 'flex';

    const handleConfirm = () => {
      cleanup();
      resolve(true);
    };

    const handleCancel = () => {
      cleanup();
      resolve(false);
    };

    const cleanup = () => {
      confirmDeleteButton.removeEventListener('click', handleConfirm);
      confirmCancelButton.removeEventListener('click', handleCancel);
      confirmBackdrop.style.display = 'none';
    };

    confirmDeleteButton.addEventListener('click', handleConfirm);
    confirmCancelButton.addEventListener('click', handleCancel);
  });
}

function bindEvents() {
  newTaskButton.addEventListener('click', () => openModal());
  taskForm.addEventListener('submit', handleFormSubmit);
  taskCancelButton.addEventListener('click', closeModal);
  statusFilter.addEventListener('change', renderTasks);
  priorityFilter.addEventListener('change', renderTasks);
  taskDialog.addEventListener('cancel', (event) => {
    event.preventDefault();
    closeModal();
  });
}

window.addEventListener('DOMContentLoaded', async () => {
  bindEvents();
  await fetchTasks();
});
