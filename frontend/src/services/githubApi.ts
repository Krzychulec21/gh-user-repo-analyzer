import type { RepositoryResponse } from '../types/github';

const API_BASE_URL = 'http://localhost:8080/api/v1/github';

export const getUserRepositories = async (username: string): Promise<RepositoryResponse[]> => {
  const response = await fetch(`${API_BASE_URL}/users/${username}/repos`);

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || 'Failed to fetch repositories');
  }

  return response.json();
};
