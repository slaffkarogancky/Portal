package kharkov.kp.gic.domain.dto;

import java.util.List;

import kharkov.kp.gic.domain.AdvConstr;

public class AdvConverter {

	public static AdvGeoJsonProperties convert(AdvConstr source, double longitude, double latitude, List<Integer> blobs) {
		// @formatter:off
		AdvGeoJsonProperties result = AdvGeoJsonProperties.builder()		
											  .id(source.getConstructionId())		
											  .customer(_nullifyIfNeed(source.getCustomer()))
											  .customerCode(_nullifyIfNeed(source.getCustomerCode()))
											  .email(_nullifyIfNeed(source.getEmail()))
											  .phone(_nullifyIfNeed(source.getPhone()))
											  .address(_nullifyIfNeed(source.getAddress()))
											  .crossAddress(_nullifyIfNeed(source.getCrossAddress()))
											  .constructionTypeId(source.getConstructionTypeId())
											  .sizes(_nullifyIfNeed(source.getSizes()))
											  .permintNumber(source.getPermintNumber())
											  .permitUntill(source.getPermitUntill())
											  .longitude(longitude)
											  .latitude(latitude)
											  .build();
		if ((blobs != null) && (blobs.size() > 0)) {
			result.setBlobs(blobs);
		}
		return result;
		// @formatter:on
	}
	
	
	private static String _nullifyIfNeed(String source) {
		if (source == null)
			return null;
		return source.trim().length() == 0 ? null : source.trim();
	}
}
