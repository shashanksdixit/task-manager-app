const TaskSummary = ({ counts }) => {
  return (
    <div className="task-summary">
      <span className="summary-chip summary-total">
        Total: {counts.total}
      </span>
      <span className="summary-chip summary-todo">TODO: {counts.todo}</span>
      <span className="summary-chip summary-progress">
        IN_PROGRESS: {counts.inProgress}
      </span>
      <span className="summary-chip summary-complete">
        COMPLETE: {counts.complete}
      </span>
    </div>
  );
};

export default TaskSummary;
