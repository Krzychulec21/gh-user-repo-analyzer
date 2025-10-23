export interface BranchResponse {
  name: string;
  lastCommitSha: string;
}

export interface RepositoryResponse {
  name: string;
  ownerLogin: string;
  branches: BranchResponse[];
}

export interface ErrorResponse {
  status: number;
  message: string;
}
