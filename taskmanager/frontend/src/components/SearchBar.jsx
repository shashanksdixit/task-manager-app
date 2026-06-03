import React from 'react';

const SearchBar = ({ filters, onFilterChange, onClear }) => {
  const handleKeywordChange = (event) => {
    onFilterChange({ ...filters, keyword: event.target.value });
  };

  const handleStatusChange = (event) => {
    onFilterChange({ ...filters, status: event.target.value });
  };

  const handlePriorityChange = (event) => {
    onFilterChange({ ...filters, priority: event.target.value });
  };

  return (
    <div className="search-bar">
      <input
        type="text"
        className="search-input"
        placeholder="Search by keyword..."
        value={filters.keyword}
        onChange={handleKeywordChange}
      />

      <select
        className="filter-select"
        value={filters.status}
        onChange={handleStatusChange}
      >
        <option value="">All Statuses</option>
        <option value="TODO">TODO</option>
        <option value="IN_PROGRESS">IN_PROGRESS</option>
        <option value="COMPLETE">COMPLETE</option>
      </select>

      <select
        className="filter-select"
        value={filters.priority}
        onChange={handlePriorityChange}
      >
        <option value="">All Priorities</option>
        <option value="LOW">LOW</option>
        <option value="MEDIUM">MEDIUM</option>
        <option value="HIGH">HIGH</option>
      </select>

      <button type="button" className="btn-clear" onClick={onClear}>
        Clear
      </button>
    </div>
  );
};

export default SearchBar;
