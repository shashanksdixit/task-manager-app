import React, { useState, useEffect } from 'react';
import SearchBar from './components/SearchBar';
import TaskList from './components/TaskList';
import TaskForm from './components/TaskForm';
import ConfirmDialog from './components/ConfirmDialog';
import {
  fetchTasks,
  createTask,
  updateTask,
  deleteTask,
  changeTaskStatus,
} from './api';
import './App.css';

function App() {
  const [tasks, setTasks] = useState([]);
  const [filters, setFilters] = useState({ keyword: '', status: '', priority: '' });
  const [editingTask, setEditingTask] = useState(null);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [deletingTaskId, setDeletingTaskId] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const loadTasks = async () => {
    setLoading(true);
    setError(null);

    try {
      const result = await fetchTasks(filters);
      setTasks(result || []);
    } catch (err) {
      setError(err?.message || 'Failed to load tasks.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTasks();
  }, [filters]);

  const handleOpenCreate = () => {
    setEditingTask(null);
    setIsFormOpen(true);
  };

  const handleOpenEdit = (task) => {
    setEditingTask(task);
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setIsFormOpen(false);
    setEditingTask(null);
  };

  const handleSave = async (formData) => {
    try {
      if (editingTask) {
        await updateTask(editingTask.id, formData);
      } else {
        await createTask(formData);
      }
      await loadTasks();
      handleCloseForm();
    } catch (err) {
      setError(err?.message || 'Failed to save task.');
    }
  };

  const handleDeleteClick = (taskId) => {
    setDeletingTaskId(taskId);
  };

  const handleDeleteConfirm = async () => {
    if (deletingTaskId === null) {
      return;
    }

    try {
      await deleteTask(deletingTaskId);
      await loadTasks();
    } catch (err) {
      setError(err?.message || 'Failed to delete task.');
    } finally {
      setDeletingTaskId(null);
    }
  };

  const handleDeleteCancel = () => {
    setDeletingTaskId(null);
  };

  const handleStatusChange = async (taskId, newStatus) => {
    try {
      await changeTaskStatus(taskId, newStatus);
      await loadTasks();
    } catch (err) {
      setError(err?.message || 'Failed to update task status.');
    }
  };

  const handleFilterChange = (newFilters) => {
    setFilters(newFilters);
  };

  const handleClearFilters = () => {
    setFilters({ keyword: '', status: '', priority: '' });
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>Task Manager</h1>
        <button type="button" onClick={handleOpenCreate}>
          + New Task
        </button>
      </header>

      {error && <div className="error-banner">{error}</div>}

      <SearchBar
        filters={filters}
        onFilterChange={handleFilterChange}
        onClear={handleClearFilters}
      />

      <TaskList
        tasks={tasks}
        loading={loading}
        onEdit={handleOpenEdit}
        onDelete={handleDeleteClick}
        onStatusChange={handleStatusChange}
      />

      <TaskForm
        isOpen={isFormOpen}
        task={editingTask}
        onSave={handleSave}
        onCancel={handleCloseForm}
      />

      <ConfirmDialog
        isOpen={deletingTaskId !== null}
        message="Are you sure you want to delete this task?"
        onConfirm={handleDeleteConfirm}
        onCancel={handleDeleteCancel}
      />
    </div>
  );
}

export default App;
