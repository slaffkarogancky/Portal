package kharkov.kp.gic.domain.dto;

import kharkov.kp.gic.domain.DocProfItem;

public class DocProfConverter {

	public static DocProfGeoJsonProperties convert(DocProfItem source) {
		// @formatter:off
		DocProfGeoJsonProperties result = DocProfGeoJsonProperties.builder()
											.id(source.getId())	
											//.cartDate(source.getCartDate())
											.carDate(Utils.formatDate(source.getCartDate()))
											.statusId(source.getStatusId())
											.indTema(Utils.nullifyIfNeed(source.getIndTema()))
											.cardNumber(Utils.nullifyIfNeed(source.getCardNumber()))
											.themeName(Utils.nullifyIfNeed(source.getThemeName()))		
											.longitude(source.getX())
											.latitude(source.getY())
											.build();			  
		return result;
		// @formatter:on
	}
}
