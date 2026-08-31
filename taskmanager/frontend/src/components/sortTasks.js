const PRIORITY_ORDER = { HIGH: 0, MEDIUM: 1, LOW: 2 };

const toDay = (dateStr) => {
  if (!dateStr) {
    return null;
  }
  const date = new Date(`${dateStr}T00:00:00`);
  return Number.isNaN(date.getTime()) ? null : date.getTime();
};

const isComplete = (task) => task?.status === 'COMPLETE';

const sortTasks = (tasks) => {
  if (!tasks || tasks.length === 0) {
    return [];
  }

  return [...tasks].sort((a, b) => {
    const aComplete = isComplete(a);
    const bComplete = isComplete(b);
    if (aComplete !== bComplete) {
      return aComplete ? 1 : -1;
    }

    const priorityDiff =
      (PRIORITY_ORDER[a?.priority] ?? PRIORITY_ORDER.MEDIUM) -
      (PRIORITY_ORDER[b?.priority] ?? PRIORITY_ORDER.MEDIUM);
    if (priorityDiff !== 0) {
      return priorityDiff;
    }

    const aDue = toDay(a?.dueDate);
    const bDue = toDay(b?.dueDate);
    if (aDue === null && bDue === null) {
      return 0;
    }
    if (aDue === null) {
      return 1;
    }
    if (bDue === null) {
      return -1;
    }
    return aDue - bDue;
  });
};

export default sortTasks;
