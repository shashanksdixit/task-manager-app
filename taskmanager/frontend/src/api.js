const API_BASE = '/api/tasks';

const handleResponse = async (response) => {
  if (response.ok) {
    return response.status === 204 ? null : response.json();
  }

  let errorMessage = response.statusText;
  try {
    const errorData = await response.json();
    if (errorData && errorData.message) {
      errorMessage = errorData.message;
    }
  } catch (error) {
    // Ignore JSON parse errors and keep statusText
  }

  throw new Error(errorMessage);
};

export async function fetchTasks(filters = {}) {
  const params = new URLSearchParams();

  if (filters.keyword) {
    params.append('keyword', filters.keyword);
  }
  if (filters.status) {
    params.append('status', filters.status);
  }
  if (filters.priority) {
    params.append('priority', filters.priority);
  }

  const queryString = params.toString();
  const url = queryString ? `${API_BASE}?${queryString}` : API_BASE;

  const response = await fetch(url, {
    method: 'GET',
  });

  return handleResponse(response);
}

export async function createTask(taskData) {
  const response = await fetch(API_BASE, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(taskData),
  });

  return handleResponse(response);
}

export async function updateTask(id, taskData) {
  const response = await fetch(`${API_BASE}/${encodeURIComponent(id)}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(taskData),
  });

  return handleResponse(response);
}

export async function deleteTask(id) {
  const response = await fetch(`${API_BASE}/${encodeURIComponent(id)}`, {
    method: 'DELETE',
  });

  await handleResponse(response);
}

export async function changeTaskStatus(id, status) {
  const response = await fetch(`${API_BASE}/${encodeURIComponent(id)}/status`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ status }),
  });

  return handleResponse(response);
}
