import React from 'react';
import StatusSelector from './StatusSelector';

const truncateText = (text, maxLength) => {
  if (!text) {
    return '';
  }
  return text.length > maxLength ? `${text.slice(0, maxLength)}...` : text;
};

const formatCreatedDate = (value) => {
  if (!value) {
    return '';
  }
  const date = new Date(value);
  return Number.isNaN(date.getTime())
    ? ''
    : date.toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' });
};

const TaskCard = ({ task, onEdit, onDelete, onStatusChange }) => {
  const isPastDue = task.dueDate && new Date(task.dueDate) < new Date();
  const priorityClass = `priority-${task.priority?.toLowerCase() || 'medium'}`;

  return (
    <div className="task-card">
      <div className="task-title">{task.title}</div>
      {task.description && (
        <div className="task-description">{truncateText(task.description, 100)}</div>
      )}

      <div className="task-meta">
        <span className={`priority-badge ${priorityClass}`}>{task.priority || 'MEDIUM'}</span>
        {formatCreatedDate(task.createdAt) && (
          <span className="created-date">
            Created: {formatCreatedDate(task.createdAt)}
          </span>
        )}
        {task.dueDate && (
          <span className="due-date">
            Due: {task.dueDate}{' '}
            {isPastDue && <span className="due-date-warning">⚠️ Past due</span>}
          </span>
        )}
      </div>

      <StatusSelector
        taskId={task.id}
        currentStatus={task.status}
        onStatusChange={onStatusChange}
      />

      <div className="task-actions">
        <button type="button" className="btn-edit" onClick={() => onEdit(task)}>
          Edit
        </button>
        <button type="button" className="btn-delete" onClick={() => onDelete(task.id)}>
          Delete
        </button>
      </div>
    </div>
  );
};

export default TaskCard;
