import TaskCard from './TaskCard';

const UrgentSection = ({ urgentTasks, onEdit, onDelete, onStatusChange }) => {
  if (!urgentTasks || urgentTasks.length === 0) {
    return null;
  }

  return (
    <div className="urgent-section">
      <h2 className="urgent-section-title">Urgent (due within 24h)</h2>
      <div className="urgent-section-list">
        {urgentTasks.map((task) => (
          <TaskCard
            key={task.id}
            task={task}
            onEdit={onEdit}
            onDelete={onDelete}
            onStatusChange={onStatusChange}
          />
        ))}
      </div>
    </div>
  );
};

export default UrgentSection;
