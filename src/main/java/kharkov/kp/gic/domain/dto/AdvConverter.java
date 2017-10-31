package kharkov.kp.gic.domain.dto;

import java.util.List;

import kharkov.kp.gic.domain.AdvConstr;

public class AdvConverter {

	public static AdvGeoJsonProperties convert(AdvConstr source, double longitude, double latitude,
			List<Integer> blobs) {
		// @formatter:off
		AdvGeoJsonProperties result = AdvGeoJsonProperties.builder()		
											  .id(source.getConstructionId())		
											  .customer(Utils.nullifyIfNeed(source.getCustomer()))
											  .customerCode(Utils.nullifyIfNeed(source.getCustomerCode()))
											  .email(Utils.nullifyIfNeed(source.getEmail()))
											  .phone(Utils.nullifyIfNeed(source.getPhone()))
											  .address(Utils.nullifyIfNeed(source.getAddress()))
											  .crossAddress(Utils.nullifyIfNeed(source.getCrossAddress()))
											  .constructionTypeId(source.getConstructionTypeId())
											  .globalTypeId(_getGlobalTypeId(source.getConstructionTypeId()))
											  .sizes(Utils.nullifyIfNeed(source.getSizes()))
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

	private static int _getGlobalTypeId(int typeId) {
		switch (typeId) {
			case 3: // Зонт
			case 4: // Информационный стенд
			case 8: // Нетрадиционные
			case 9: // Объем-простр.констр.
			case 10: // Остановочный павильон
			case 12: // Сцена
			case 15: // Флаг
			case 16: // Щит выносной
			case 18: // Щит на метро
			case 22: // Щит стационарный
			case 59: // Проекционная установка
				return 0; // НАЗЕМНЫЕ
			case 2: // Вывеска на фасаде
			case 6: // Кронштейн на фасаде
			case 7: // Крышная установка
			case 13: // Указатель на фасаде
			case 20: // Щит на пролетных строениях
			case 21: // Щит на фасаде
			case 52: // Настенное панно
			case 57: // Щит на существующем сооружении
			case 71: // Маркиза
				return 1; // ФАСАДНЫЕ
			case 5: // Кронштейн на опоре
			case 19: // Щит на опоре
				return 2; // НА ОПОРЕ
			case 11: // Растяжка
			case 14: // Указатель над дорогой
				return 3; // НА РАСТЯЖКЕ
			case 17: // Щит на вр. огражд.
			case 50: // Щит на ограждении
			case 54: // Щит на пешеходном ограждении
				return 4; // НА ОГРАЖДЕНИИ
			default:
				return 0;
			}
	}
}
