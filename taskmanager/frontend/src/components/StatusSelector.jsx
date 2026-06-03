import React from 'react';

const StatusSelector = ({ taskId, currentStatus, onStatusChange }) => {
  const handleChange = (event) => {
    onStatusChange(taskId, event.target.value);
  };

  return (
    <select
      className="status-selector"
      value={currentStatus}
      data-status={currentStatus}
      onChange={handleChange}
    >
      <option value="TODO">To Do</option>
      <option value="IN_PROGRESS">In Progress</option>
      <option value="COMPLETE">Complete</option>
    </select>
  );
};

export default StatusSelector;
