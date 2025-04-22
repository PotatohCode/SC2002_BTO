package BTO;

import java.util.List;

public interface Admin {
	
	default boolean clashApplication(BTO bto, List<BTO> managingBTO) {
		for (BTO managing : managingBTO) {
			if ((bto.getApplicationStart().after(managing.getApplicationStart()) && bto.getApplicationStart().before(managing.getApplicationEnd())
					|| (bto.getApplicationEnd().after(managing.getApplicationStart()) && bto.getApplicationEnd().before(managing.getApplicationEnd())))) {
				return true;
			}
		}
		return false;
	}
	
	public void managingBTO(List<Integer> managingId, Project<BTO> btoProj, Project<Application> appProj, Project<Enquiries> enquiryProj) throws InvalidInput;
	
	
}
