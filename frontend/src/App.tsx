import { useState } from 'react';
import { SearchBar } from './components/SearchBar';
import { RepositoryCard } from './components/RepositoryCard';
import { getUserRepositories } from './services/githubApi';
import type { RepositoryResponse } from './types/github';
import './App.css';

function App() {
  const [repositories, setRepositories] = useState<RepositoryResponse[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [searchedUsername, setSearchedUsername] = useState<string | null>(null);

  const handleSearch = async (username: string) => {
    setIsLoading(true);
    setError(null);
    setRepositories([]);
    setSearchedUsername(username);

    try {
      const data = await getUserRepositories(username);
      setRepositories(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'An unexpected error occurred');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1 className="app-title">GitHub Repository Analyzer</h1>
        <p className="app-subtitle">Explore repositories and branches for any GitHub user</p>
      </header>

      <main className="app-main">
        <SearchBar onSearch={handleSearch} isLoading={isLoading} />

        {error && (
          <div className="error-message">
            <svg className="error-icon" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <p>{error}</p>
          </div>
        )}

        {isLoading && (
          <div className="loading-container">
            <div className="loading-spinner"></div>
            <p>Loading repositories...</p>
          </div>
        )}

        {!isLoading && !error && repositories.length > 0 && (
          <div className="results-section">
            <h2 className="results-title">
              Found {repositories.length} {repositories.length === 1 ? 'repository' : 'repositories'} for {searchedUsername}
            </h2>
            <div className="repositories-grid">
              {repositories.map((repo) => (
                <RepositoryCard key={repo.name} repository={repo} />
              ))}
            </div>
          </div>
        )}

        {!isLoading && !error && searchedUsername && repositories.length === 0 && (
          <div className="empty-state">
            <p>No repositories found for {searchedUsername}</p>
          </div>
        )}
      </main>
    </div>
  );
}

export default App;
