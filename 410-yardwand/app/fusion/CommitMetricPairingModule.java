package fusion;

import models.Repository;

public class CommitMetricPairingModule {
	public Repository repo1;
	public Repository repo2;
	
	public CommitMetricPairingModule(String repopath1, String repopath2) {
		repo1 = new Repository(repopath1);
		repo2 = new Repository(repopath2);
	}

}
