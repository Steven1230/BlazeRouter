package com.fico.blaze.router;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;
import rma.XMLJSONConverter;

@SpringBootTest
class BlazeRouterApplicationTests {

	@Test
	void testNameSpaceXML() throws Exception {

		XMLJSONConverter xmljsonConvertor = new XMLJSONConverter(ResourceUtils.getFile("classpath:ApplicationNS.xsd"));

		String testJSON = "{\"appID\":\"12345\",\"applicant\":{\"age\":0,\"name\":\"test\"},\"decisionResponseList\":[{\"name\":\"des1\"}]}";

		String testNSXML = "<abc:Application AppNo=\"string\" CallStage=\"string\" AppDate=\"2008-09-29\" ProductCode=\"string\" RandomDigit01=\"100\" RandomDigit02=\"100\" RandomDigit03=\"100\" RandomDigit04=\"100\" RandomDigit05=\"100\" RandomDigit06=\"100\" RandomDigit07=\"100\" RandomDigit08=\"100\" RandomDigit09=\"100\" RandomDigit10=\"100\" xmlns:abc=\"http://www.blazesoft.com/ABC\">\r\n" +
				"  <!--Optional:-->\r\n" +
				"  <abc:DecisionResponse DecisionResult=\"100\" SystemRecommendLimit=\"1.051732E7\" ApprovalLimit=\"1.051732E7\" Score01=\"1.051732E7\" Score02=\"1.051732E7\" Score03=\"1.051732E7\" Score04=\"1.051732E7\" Score05=\"1.051732E7\" Score06=\"1.051732E7\" Score07=\"1.051732E7\" Score08=\"1.051732E7\" Score09=\"1.051732E7\" Score10=\"1.051732E7\" ReservedNumericField01=\"1.051732E7\" ReservedNumericField02=\"1.051732E7\" ReservedNumericField03=\"1.051732E7\" ReservedNumericField04=\"1.051732E7\" ReservedNumericField05=\"1.051732E7\" ReservedNumericField06=\"1.051732E7\" ReservedNumericField07=\"1.051732E7\" ReservedNumericField08=\"1.051732E7\" ReservedNumericField09=\"1.051732E7\" ReservedNumericField10=\"1.051732E7\" ReservedNumericField11=\"1.051732E7\" ReservedNumericField12=\"1.051732E7\" ReservedNumericField13=\"1.051732E7\" ReservedNumericField14=\"1.051732E7\" ReservedNumericField15=\"1.051732E7\" ReservedNumericField16=\"1.051732E7\" ReservedNumericField17=\"1.051732E7\" ReservedNumericField18=\"1.051732E7\" ReservedNumericField19=\"1.051732E7\" ReservedNumericField20=\"1.051732E7\" ReservedStringField01=\"string\" ReservedStringField02=\"string\" ReservedStringField03=\"string\" ReservedStringField04=\"string\" ReservedStringField05=\"string\" ReservedStringField06=\"string\" ReservedStringField07=\"string\" ReservedStringField08=\"string\" ReservedStringField09=\"string\" ReservedStringField10=\"string\" ReservedStringField11=\"string\" ReservedStringField12=\"string\" ReservedStringField13=\"string\" ReservedStringField14=\"string\" ReservedStringField15=\"string\" ReservedStringField16=\"string\" ReservedStringField17=\"string\" ReservedStringField18=\"string\" ReservedStringField19=\"string\" ReservedStringField20=\"string\">\r\n" +
				"    <!--Zero or more repetitions:-->\r\n" +
				"    <abc:ScoreModelReturnInfo ScoreModelName=\"string\" Score=\"1.051732E7\" PrecalculateScore=\"true\" InitialScore=\"1.051732E7\" UnexpectedCount=\"100\" CharacteristicCount=\"100\">\r\n" +
				"      <!--Zero or more repetitions:-->\r\n" +
				"      <abc:ScoredCharacteristic CharacteristicName=\"string\" BinLabel=\"string\" BaselineScore=\"1.051732E7\" PartialScore=\"1.051732E7\" MaxBinScore=\"1.051732E7\" Unexpected=\"true\" WminBinScore=\"1.051732E7\"/>\r\n" +
				"    </abc:ScoreModelReturnInfo>\r\n" +
				"    <!--Zero or more repetitions:-->\r\n" +
				"    <abc:RuleReason RuleCode=\"string\" RuleDescription=\"string\" RuleDecision=\"string\"/>\r\n" +
				"  </abc:DecisionResponse>\r\n" +
				"  <!--Optional:-->\r\n" +
				"  <abc:MessageList StrategyVersion=\"string\" StatusCode=\"100\" StatusDescription=\"string\" ElapsedMillis=\"100\">\r\n" +
				"    <!--Zero or more repetitions:-->\r\n" +
				"    <abc:Message Description=\"string\"/>\r\n" +
				"  </abc:MessageList>\r\n" +
				"  <abc:Applicant Age=\"100\" ApplicantType=\"string\" CurrentGrantLimit=\"1.051732E7\" Occupation=\"string\" NumDirectRelatedOverdueAcct=\"100\" IsIdCardHitBlackList=\"100\" FaceCheckRate=\"1.051732E7\" isEmergencyContactOverdue=\"100\" IsHitManualRejectRules=\"100\" NumOrdersFromPhoneInXdays=\"100\" NumDirectRelatedOprBlackList=\"100\" NumDirectRelatedCourtBlackList=\"100\" DirectNumTongdunRejectAcct=\"100\" CurrentAcctStatus=\"string\" DirectNumP2PRejectAcct=\"100\" IsIdCardHitCourtBlackList=\"100\" IsIdCardHitfinancialBlackList=\"100\" ZhichengScore=\"1.051732E7\" isHitOutterCreditRiskBlackList=\"100\" UnionPayScore=\"1.051732E7\" TongdunRules=\"1.051732E7\" XinyanRules=\"1.051732E7\" NumQueryTongDun=\"100\" IsHitTongdunAcctMList=\"100\" IsHitTongdunWatchList=\"100\" AcctClassifyRules=\"100\" AliYunScore=\"1.051732E7\" HotelInstalmentAmt=\"1.051732E7\" RegisterPhone=\"string\" NumReg7DaysSamePhone=\"100\" MOB=\"100\" MaxHistoryDue=\"100\" RiskAmount=\"1.051732E7\" NumOverdueHistory=\"100\" InstallmentScore=\"100\">\r\n" +
				"    <!--Optional:-->\r\n" +
				"    <abc:MobileInfo NumCommME9=\"100\" PhoneOnlineTime=\"100\" PhoneBelongArea=\"string\" IsPhoneInBlackList=\"100\" isDeviceHitBlackList=\"100\" phoneLocationAddr=\"string\" NumAcctCallLogRelated=\"100\" OfflineRate=\"1.051732E7\"/>\r\n" +
				"    <!--Optional:-->\r\n" +
				"    <abc:OrderInfo ReceivingAdress=\"string\" ReceivingPhone=\"string\" FirstLevelClassify=\"string\" RecipientAdress=\"string\" IsRecipientPhoneHitBlackList=\"100\" IsRecipientAddrHitBlackList=\"100\" OverdueRateSameAddr=\"1.051732E7\" RecipientPhoneAcctMinAmount=\"1.051732E7\" IsRecipientOverdue=\"100\" IsRecipientPhoneHitP2PBlackList=\"100\" IsRecivingAddrEqualRegPhoneLocateAddr=\"100\" OrderAmount=\"1.051732E7\" OrderPrefix=\"string\"/>\r\n" +
				"    <!--Optional:-->\r\n" +
				"    <abc:BehaviorInfo NumOverduceLast3Mon=\"100\" NumMaxDueDaysLast3Mon=\"100\" NumOverduceLast6Mon=\"100\" NumMaxDueDaysLast6Mon=\"100\" NumOverduceLast12Mon=\"100\" NumMaxDueDaysLast12Mon=\"100\"/>\r\n" +
				"  </abc:Applicant>\r\n" +
				"</abc:Application>";
	}

	@Test
	void testNoNameSpaceXML() throws Exception {

		XMLJSONConverter xmljsonConvertor = new XMLJSONConverter(ResourceUtils.getFile("classpath:Application.xsd"));

		String testJSON = "{\"appID\":\"12345\",\"applicant\":{\"age\":0,\"name\":\"test\"},\"decisionResponseList\":[{\"name\":\"des1\"}]}";

		String testAppXML = "<Application AppNo=\"string\" CallStage=\"string\" RandomDigit01=\"100\" RandomDigit02=\"100\" RandomDigit03=\"100\" RandomDigit04=\"100\" RandomDigit05=\"100\" RandomDigit06=\"100\" RandomDigit07=\"100\" RandomDigit08=\"100\" RandomDigit09=\"100\" RandomDigit10=\"100\">\r\n" +
				"  <!--Optional:-->\r\n" +
				"  <DecisionResponse DecisionResult=\"100\" SystemRecommendLimit=\"1.051732E7\" ApprovalLimit=\"1.051732E7\" Score01=\"1.051732E7\" Score02=\"1.051732E7\" Score03=\"1.051732E7\" Score04=\"1.051732E7\" Score05=\"1.051732E7\" Score06=\"1.051732E7\" Score07=\"1.051732E7\" Score08=\"1.051732E7\" Score09=\"1.051732E7\" Score10=\"1.051732E7\" ReservedNumericField01=\"1.051732E7\" ReservedNumericField02=\"1.051732E7\" ReservedNumericField03=\"1.051732E7\" ReservedNumericField04=\"1.051732E7\" ReservedNumericField05=\"1.051732E7\" ReservedNumericField06=\"1.051732E7\" ReservedNumericField07=\"1.051732E7\" ReservedNumericField08=\"1.051732E7\" ReservedNumericField09=\"1.051732E7\" ReservedNumericField10=\"1.051732E7\" ReservedNumericField11=\"1.051732E7\" ReservedNumericField12=\"1.051732E7\" ReservedNumericField13=\"1.051732E7\" ReservedNumericField14=\"1.051732E7\" ReservedNumericField15=\"1.051732E7\" ReservedNumericField16=\"1.051732E7\" ReservedNumericField17=\"1.051732E7\" ReservedNumericField18=\"1.051732E7\" ReservedNumericField19=\"1.051732E7\" ReservedNumericField20=\"1.051732E7\" ReservedStringField01=\"string\" ReservedStringField02=\"string\" ReservedStringField03=\"string\" ReservedStringField04=\"string\" ReservedStringField05=\"string\" ReservedStringField06=\"string\" ReservedStringField07=\"string\" ReservedStringField08=\"string\" ReservedStringField09=\"string\" ReservedStringField10=\"string\" ReservedStringField11=\"string\" ReservedStringField12=\"string\" ReservedStringField13=\"string\" ReservedStringField14=\"string\" ReservedStringField15=\"string\" ReservedStringField16=\"string\" ReservedStringField17=\"string\" ReservedStringField18=\"string\" ReservedStringField19=\"string\" ReservedStringField20=\"string\">\r\n" +
				"    <!--Zero or more repetitions:-->\r\n" +
				"    <ScoreModelReturnInfo ScoreModelName=\"string\" Score=\"1.051732E7\" PrecalculateScore=\"false\" InitialScore=\"1.051732E7\" UnexpectedCount=\"100\" CharacteristicCount=\"100\">\r\n" +
				"      <!--Zero or more repetitions:-->\r\n" +
				"      <ScoredCharacteristic CharacteristicName=\"string\" BinLabel=\"string\" BaselineScore=\"1.051732E7\" PartialScore=\"1.051732E7\" MaxBinScore=\"1.051732E7\" Unexpected=\"true\" WminBinScore=\"1.051732E7\"/>\r\n" +
				"    </ScoreModelReturnInfo>\r\n" +
				"    <!--Zero or more repetitions:-->\r\n" +
				"    <RejectCode>string</RejectCode>\r\n" +
				"    <!--Zero or more repetitions:-->\r\n" +
				"    <ManualCode>string</ManualCode>\r\n" +
				"    <!--Zero or more repetitions:-->\r\n" +
				"    <DataSourceCode>string</DataSourceCode>\r\n" +
				"  </DecisionResponse>\r\n" +
				"  <!--Optional:-->\r\n" +
				"  <MessageList StrategyVersion=\"string\" StatusCode=\"100\" StatusDescription=\"string\" ElapsedMillis=\"100\">\r\n" +
				"    <!--Zero or more repetitions:-->\r\n" +
				"    <Message Description=\"string\"/>\r\n" +
				"  </MessageList>\r\n" +
				"</Application>";

	}

}
