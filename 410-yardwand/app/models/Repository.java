package models;

import fusion.InvalidRepositoryURLException;

public class Repository {
	
	private String url;
	/*
	 * For now, only have support for github repositories.
	 * This regex is rather lazy but should get the job done.
	 */
	private static final String URL_REGEX = "^https://github\\.com/[^/]*/[^/]*$";
	
	/**
	 * Create a repository object without setting the URL.
	 * Must later set the url with <code>setUrl(String url)</code>
	 */
	public Repository() {}
	
	/**
	 * Creates a Repository object corresponding to a github repository.
	 * URL should look like: https://github.com/SebastianKG/410-yardwand
	 * For now, only github repositories are allowed.
	 * @param url
	 * @throws InvalidRepositoryURLException 
	 */
	public Repository(String url) throws InvalidRepositoryURLException {
		if (!url.matches(URL_REGEX))
			throw new InvalidRepositoryURLException("Given URL is not a valid GitHub repository URL.");
		this.url = url;
	}
	
	public void setUrl(String url) throws InvalidRepositoryURLException {
		if (!url.matches(URL_REGEX))
			throw new InvalidRepositoryURLException("Given URL is not a valid GitHub repository URL.");
		this.url = url;
	}
	
	/**
	 * Get the repository base URL.
	 * @param url
	 * @return
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Get a URL in the format that <code>NewCollaboratorDetector</code> expects.
	 * Change a URL like: https://github.com/SebastianKG/410-yardwand
	 * to: https://api.github.com/repos/SebastianKG/410-yardwand/stats/contributors
	 * @param url
	 */
	public String getContributorURL() {
		String[] fragments = url.split("github.com/");
		fragments[0] += "api.";
		fragments[1] = "repos/" + fragments[1];
		return fragments[0] + "github.com/" + fragments[1] + "/stats/contributors";
	}
}
