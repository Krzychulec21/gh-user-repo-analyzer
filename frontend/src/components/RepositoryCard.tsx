import type { RepositoryResponse } from '../types/github';

interface RepositoryCardProps {
  repository: RepositoryResponse;
}

export const RepositoryCard = ({ repository }: RepositoryCardProps) => {
  return (
    <div className="repository-card">
      <div className="repository-header">
        <h3 className="repository-name">{repository.name}</h3>
        <span className="repository-owner">{repository.ownerLogin}</span>
      </div>

      <div className="branches-section">
        <h4 className="branches-title">Branches ({repository.branches.length})</h4>
        <div className="branches-list">
          {repository.branches.map((branch) => (
            <div key={branch.name} className="branch-item">
              <div className="branch-name">{branch.name}</div>
              <div className="commit-sha">{branch.lastCommitSha.substring(0, 7)}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
