import React from 'react';
import TaskCard from './TaskCard';

const TaskList = ({ tasks, loading, onEdit, onDelete, onStatusChange }) => {
  if (loading) {
    return <div className="loading">Loading tasks...</div>;
  }

  if (!tasks || tasks.length === 0) {
    return <div className="empty-state">No tasks found.</div>;
  }

  return (
    <div className="task-list">
      {tasks.map((task) => (
        <TaskCard
          key={task.id}
          task={task}
          onEdit={onEdit}
          onDelete={onDelete}
          onStatusChange={onStatusChange}
        />
      ))}
    </div>
  );
};

export default TaskList;
