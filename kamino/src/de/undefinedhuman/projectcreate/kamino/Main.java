package de.undefinedhuman.projectcreate.kamino;

import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.server.plugin.Plugin;

public class Main extends Plugin {

    @Override
    public void load() {
        Log.info("Hello");
    }

    @Override
    public void init() {

    }

    @Override
    public void delete() {

    }

/*    public static void main(String[] args) throws IOException {
        Cluster cluster = Cluster.connect("127.0.0.1", "undefinedhuman", "VrHsWZQbeKwDkBCJv8ARKebaAGEKNy3k");
        Bucket bucket = cluster.bucket("server_name");
        Scope scope = bucket.scope("projectcreate-kamino");
        Collection data = scope.collection("data");
        Collection meta = scope.collection("meta");
        byte[] fakeData = createFakeData();
        data.upsert("uncompressed", fakeData, UpsertOptions.upsertOptions().transcoder(RawBinaryTranscoder.INSTANCE));
        LZ4Compressor compressor = LZ4Factory.fastestInstance().fastCompressor();
        byte[] compressedData = compressor.compress(fakeData);
        meta.upsert("compressed", compressedData, UpsertOptions.upsertOptions().transcoder(RawBinaryTranscoder.INSTANCE));
    }

    private static final int MINUTES = 5;
    private static final int NUMBER_OF_PLAYERS = 100;

    private static byte[] createFakeData() throws IOException {
        HashSet<EventType> eventTypesUsed = new HashSet<>();
        HashSet<String> uuidsUsed = new HashSet<>();
        ArrayList<String> logLines = new ArrayList<>();
        String[] uuids = UUID.UUIDs.split("\n");

        for(int seconds = 0; seconds < MINUTES * 60; seconds++) {
            for(int i = 0; i < NUMBER_OF_PLAYERS; i++) {
                EventType eventType = EventType.values()[new Random().nextInt(EventType.values().length)];
                eventTypesUsed.add(eventType);
                String uuid = uuids[i%uuids.length];
                uuidsUsed.add(uuid);
                Optional<String> optional = Arrays.stream(generateLogLine(seconds, eventType, uuid).split("\n")).map(String::trim).reduce(String::concat);
                if(optional.isEmpty())
                    continue;
                logLines.add(optional.get() + "\n");
            }
        }

        // write to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        for (String element : logLines)
            out.writeUTF(element);
        return baos.toByteArray();
    }

    enum EventType {
        BLOCK_BREAK_EVENT,
        ITEM_TRANSFER_EVENT,
        ITEM_DROP_EVENT,
        CREATE_ENTITY,
        REMOVE_ENTITY
    }

    static class UUID {
        public static final String UUIDs =
                "4bb478af-9456-4d46-a71c-acde63a3fe14\n" +
                        "643140f0-0b65-4db1-9437-ac93a0e703cc\n" +
                        "0ec29b89-958a-4929-8823-fc75da591476\n" +
                        "d3d45213-8a57-4ef3-a8eb-fe972004142e\n" +
                        "5145e636-920b-48cf-94b4-a6cce1065da5\n" +
                        "8e38ce1b-e0b7-4af4-bf88-169aa61e29db\n" +
                        "20626929-86d9-4acd-91b7-e83c52177524\n" +
                        "224c83c2-e63c-4fff-8d59-a8d1c5909fef\n" +
                        "d0faf5d1-40cb-4047-b61a-2ffb3733fc45\n" +
                        "8908c0d3-1f76-4a25-9b04-1e277f85eb49\n" +
                        "089da6e8-fa65-4a80-a2d3-6d883a91a702\n" +
                        "2511741f-8c55-456d-9ee4-cae6a0b7c725\n" +
                        "47c7e4e2-026a-4968-98ca-8f9e0882c1f6\n" +
                        "b48b9c53-8a35-456d-99bd-dcac4de9a379\n" +
                        "53ef319e-5d22-4707-b389-baaf6ae038a3\n" +
                        "ae89e381-5e17-4a37-9ead-8fbe0c6a7cd8\n" +
                        "42eadb50-9a18-4dce-9113-3ee0ff1fbc54\n" +
                        "db8cf886-fbdb-4d8b-8af2-b2b65760e084\n" +
                        "744d78f1-d8be-4e7e-8222-2eebe9ec79e5\n" +
                        "fb491f52-f0e3-4cf3-bf7f-e53cfd9490e2\n" +
                        "d4b02b00-da40-441b-a50d-d87b267c0ebb\n" +
                        "67986e27-ff33-44d0-9935-7c727fac9bdb\n" +
                        "63368816-4bbf-41d8-9803-c6536a9cb29b\n" +
                        "1ca3c4b9-e07e-4d0a-9ed1-b0b45116c779\n" +
                        "0acbd6a8-8d59-4a6a-ab16-54b25d2cb66e\n" +
                        "44420a81-b197-4a42-ae28-6a87860b85c5\n" +
                        "b3d28998-81ee-4c3a-b7ae-3c99bd138cfc\n" +
                        "42ad021d-7b3c-4757-83ac-22d743647f72\n" +
                        "3e9d2616-8718-4ef8-97e4-d557c58e6599\n" +
                        "4ebe040d-5245-4fa4-a8c8-e4b1b435d815\n" +
                        "5c5fb82c-0daa-435a-8583-494f937ae21e\n" +
                        "c18039ad-6d21-478a-88a6-026af58cf183\n" +
                        "102ff1e0-ad27-41a0-9841-99ce8dd6e2db\n" +
                        "6e47e905-15bc-48b1-9aba-b61761c9d273\n" +
                        "ac9e482d-c8c0-4a0a-bb7c-e197f4476857\n" +
                        "31d88640-d68c-4e22-a1aa-60482501620c\n" +
                        "8cfd9ea5-bd58-4e5f-bb66-f5cad60040ba\n" +
                        "ecfbfb40-9ca7-4e98-9ebb-258c502f734e\n" +
                        "40494bb2-e2f6-4ed6-9a6e-1fa256b56918\n" +
                        "fa48da74-8b53-49ae-b68d-c6ec2c6d5d6a\n" +
                        "fa2eab40-9c56-4d92-9a69-3281b30a4ef8\n" +
                        "1d5f119f-10d8-4535-8cfd-64f17631a84f\n" +
                        "3495f587-211e-45d4-b70b-4cb56c029ece\n" +
                        "969536bd-a7cf-44df-8922-8c9e9d6ec695\n" +
                        "049d831a-450a-4df1-a20c-b072242d5bc4\n" +
                        "6d9cf690-1483-4c71-a9bb-3c8f369be5ac\n" +
                        "ad14d241-025a-41aa-b507-a3a866dfeadb\n" +
                        "03d971e3-1ae5-4795-8f52-d29cc3d1d3d1\n" +
                        "3b76019b-d9db-428d-b42d-f58247bd668d\n" +
                        "05e88c31-9d0a-4dfa-bd7a-1d2a381dcd33\n" +
                        "1c4a5b05-ce41-44c3-be0f-92a70aa5942a\n" +
                        "f7eca426-6afc-4c8d-91f7-765c5e28acd7\n" +
                        "fab16515-2972-4571-98a7-589aca6371ef\n" +
                        "15a5be57-ce13-4123-bc64-f1ac93907ac7\n" +
                        "5d8e8f28-45f0-4fbd-9535-0cb13fb2247d\n" +
                        "d1b971d3-c1d7-4e2f-ac8b-802de58a7981\n" +
                        "e723b136-574a-4cce-a87d-b2544055d457\n" +
                        "61f976da-28db-40f6-ba6a-840ccb302b58\n" +
                        "33589e01-51dc-476d-8087-b8ea0e9d64f7\n" +
                        "a69f30c0-9989-481f-bec7-b033df4f5513\n" +
                        "71c24ad3-73cc-446f-81bd-e83d7e0c099a\n" +
                        "37f36ea7-860f-46c6-84a5-e07501b5a403\n" +
                        "c3d2454c-d4bf-4076-9441-13636e771657\n" +
                        "6850d6b1-1f2a-46b6-94df-1a74cb69fec4\n" +
                        "5d65b4ac-fea3-4101-ba97-048360a34919\n" +
                        "0efe525a-f3b9-41aa-896c-77c66d0a0047\n" +
                        "64a50c3e-c1e8-440e-ace4-2c35f911880c\n" +
                        "c0ece870-cbe4-49c7-8038-9ffc39b9a8ac\n" +
                        "c08bc572-b47f-4049-a597-2f61d7413524\n" +
                        "87a353e0-91d2-4629-bc8a-d7e47ebdd4d7\n" +
                        "f9b16eb6-faa8-413b-aa85-b6a7155ed3da\n" +
                        "5362b59f-a607-459b-add5-b2bb912aa1ec\n" +
                        "1b456da1-2148-4009-a670-e4ae36f47e14\n" +
                        "f025e949-dff1-4617-8bd7-7276d4366119\n" +
                        "3231bcb0-3866-4980-ba4e-79fcfe00f281\n" +
                        "16d23f7b-9cfb-453b-ae71-b68f0591d8fe\n" +
                        "72590494-a352-4164-aee9-6f4850fc9a77\n" +
                        "d690a6de-af81-45b5-a670-7b9c8b041261\n" +
                        "090d452b-8545-40cb-9061-a4368f26554f\n" +
                        "6d592384-737f-46fa-98ee-55eb7dab73c9\n" +
                        "137d8928-2097-4ea1-b35e-2dc11e4764c2\n" +
                        "904fc542-32a6-4329-bd7e-0a246c32cee3\n" +
                        "1f4daa18-5645-4b92-a859-6ca3f180e9a7\n" +
                        "84685113-31ee-40df-aeb8-ddecd870e66a\n" +
                        "d1f088c9-d1e4-40a0-b119-c41a7aa25283\n" +
                        "1e795832-1bdf-43df-a220-e5589afc9ad0\n" +
                        "524d7e79-0700-480e-ad23-4249131aa5fe\n" +
                        "5f521d86-5903-4466-a03f-17e0801bbe6f\n" +
                        "19e58b0e-d497-4c3b-b226-27858fd859b1\n" +
                        "5ebbbf0e-f706-4fc4-964a-1cb81bdd7b54\n" +
                        "0a91390a-9f04-48d5-8b38-e5e39e112541\n" +
                        "eaea1621-f9b4-4719-8836-f15ea44ba69c\n" +
                        "41e71542-26e4-4a83-adb3-26805f384853\n" +
                        "8180be8d-0dca-482f-be24-f9348910e374\n" +
                        "066af02e-cf50-46aa-8c70-a3f9679c4251\n" +
                        "0011e5b7-bf47-46fd-9598-113e6bf52979\n" +
                        "55e1ae01-9b5f-4f00-a49d-f6548ddf9544\n" +
                        "d1c9cd20-8514-4263-b88a-885efebaa39f\n" +
                        "39f0d042-7531-43a5-87c7-0c8ca3d7d18a\n" +
                        "5422b064-59aa-42a0-998e-0f2466c8157a\n" +
                        "dc326259-f6a7-4a87-aeee-7e42c8244e73\n" +
                        "9225b928-dff5-4db1-9f8b-1a11e353efb1\n" +
                        "c2f9c4ec-e2f8-4c8d-a684-b5f8c6484057\n" +
                        "198ea647-88d6-4e42-bc04-0814c2212fe2\n" +
                        "1ef74110-69d9-453b-b47c-ff186aa17a4f\n" +
                        "b6cb177b-df95-49a2-988a-fd1171f824d4\n" +
                        "7626bc24-5076-4c9e-b1b7-9f744515f004\n" +
                        "ec3b4e90-bdb3-499c-8afd-0537b1163330\n" +
                        "4d0bdbbe-e55e-43e0-80b4-510d4035531c\n" +
                        "736b9418-e925-443f-a4e4-17776730b4d8\n" +
                        "45c5736b-3cbb-4fe0-8957-535528575ee2\n" +
                        "2fe1a095-6f5a-4d22-945e-611a4b5f9919\n" +
                        "20d74aac-274c-43c2-beef-ee8c842c9627\n" +
                        "b40e3b4c-b088-4959-9637-e0f8d037a84f\n" +
                        "c532f593-a69d-4042-b4f4-8050d107ab24\n" +
                        "b80612d1-7b15-46bd-8bb9-f275e2bbe252\n" +
                        "6c67a87e-062e-41de-bc51-f61a501d6534\n" +
                        "646fe9b5-e3d6-4c58-87e9-ff426eb59194\n" +
                        "5ac83607-25d6-4725-83cc-43fbb37e1877\n" +
                        "fd86966b-3894-4606-b5b2-6027b7e3f207\n" +
                        "de1b1ed6-d365-47d2-9194-881fa4349a8b\n" +
                        "7ca9cd40-d97b-405a-8f2e-3da0c039b7a3\n" +
                        "dca5c966-e233-4038-a78a-94ef0bfc6719\n" +
                        "ddb60158-8915-4512-a7f4-e03ed7263464\n" +
                        "12935f60-7c71-4277-ac07-823285c60e93\n" +
                        "8098a287-3285-4f9e-b848-828094428041\n" +
                        "64d3388b-f088-4d3b-bd4b-528109e54b20\n" +
                        "cfc5dde0-f078-4ac0-a65d-3ecfcc5dabfc\n" +
                        "c711b711-b6f3-4a15-850c-89ce1bc95b1a\n" +
                        "c731be61-2a6b-4958-ae6d-cc77cc3a244c\n" +
                        "6a34824c-8ce1-4599-9611-093fdbd29dfb\n" +
                        "ca5df3c8-e611-45bd-b0f1-31add5454d81\n" +
                        "c4d8f7e0-4be9-4f3c-83aa-50403f2f9c3f\n" +
                        "73573e2f-6328-43b6-9d64-b4da796e149e\n" +
                        "d1e155c5-dc28-4ba7-8ac8-15b8d11b95a6\n" +
                        "5b82890f-50f5-43d1-9c06-b3a7c11f840f\n" +
                        "a7548524-7751-4c64-a393-05c3e5b268e6\n" +
                        "b66d480f-5e88-4a6c-8324-3ad6818c071b\n" +
                        "5d0385bd-48f4-4b88-8105-ef1232934600\n" +
                        "fa3ba279-f715-448c-bc0d-03796f24d8f9\n" +
                        "336a7012-e6b7-4151-89c8-2f107356496e\n" +
                        "ccb8abc3-cde0-4824-ac23-fb288907f308\n" +
                        "6a538ea3-7327-4067-8850-8f44a0612026\n" +
                        "117790ad-4684-4de5-9795-af03d6be8f29\n" +
                        "011a7ec0-d746-48d1-afb8-69660a348a58\n" +
                        "5d603002-87c9-4201-aa8a-63aea219c17a\n" +
                        "50769d51-03ed-46ea-b79a-a7db498005d9\n" +
                        "61a9fb2d-4716-4cd6-9b46-7809198a3651\n" +
                        "69008259-0c55-40c1-8d43-22d79e653adb\n" +
                        "0bd36f58-3abe-4516-9d89-a432f4fea9cf\n" +
                        "f4d74c0f-1e7c-4fa6-ac72-0d20ff75dc6e\n" +
                        "9ccecfda-dc31-4887-bb62-4315f17d5c81\n" +
                        "0a2a18da-f372-4e98-a1af-b16d436210a7\n" +
                        "389b5011-b86e-443f-a681-0326a90eeb06\n" +
                        "16b8d168-2739-436e-80d6-c59213dc2ac2\n" +
                        "13f35945-98a2-48ba-8404-fe42ed57605c\n" +
                        "e0b323f9-26ee-4444-bfb7-98fa670475f6\n" +
                        "e7fb2421-4c4d-4bd6-b6bd-03f64a2140ea\n" +
                        "7d2cfd6b-88aa-4443-a5f0-8e29b58db84d\n" +
                        "dfb224c2-b636-4e69-8a9f-63043ac376e1\n" +
                        "63ac3fae-4f8e-4ef6-b23f-2c69ab785f50\n" +
                        "d8dcc89e-52a3-4abf-8312-ad85ac7d43d6\n" +
                        "f63a468e-6414-4243-8ad9-547f3c1ce0a1\n" +
                        "89cec74a-6167-4d1e-93ba-4a8e465d7826\n" +
                        "10d4e0b5-af09-4fa4-b01f-c3a7032c491f\n" +
                        "7a446809-ca15-4192-bd39-3bdaa30057d0\n" +
                        "508d28b9-5e11-49be-9500-3a4f274a9b34\n" +
                        "96d501d4-5ed8-4405-88f5-b9dd879a25d8\n" +
                        "92b7315f-5006-419e-96ac-b53319440a3e\n" +
                        "d6146362-e147-41b2-b12f-154a1a00b6cd\n" +
                        "67d8eb25-71a7-45fa-a95f-73e50b307804\n" +
                        "32dd2c81-c7ec-4307-a97c-658c86a61398\n" +
                        "3cee4379-839c-4186-b0b8-2f61912a340a\n" +
                        "bee235bc-be6c-45d5-a8a3-47680f4ff758\n" +
                        "20470475-f4f8-42b5-be7e-351c9b2d3cfa\n" +
                        "686b63b2-bcdb-48cf-99aa-19d6e9e2b034\n" +
                        "bbec55b6-42ea-4f23-9623-af3553a5c665\n" +
                        "afc6f8ad-1513-436a-9b3a-d2f20f9430a6\n" +
                        "171ea1d1-fe42-4845-a9ad-bfb19a92b4dd\n" +
                        "958d4d87-b9f0-4e4e-be88-b3a250829a17\n" +
                        "e18bc69b-1cb8-47c9-a6cd-5412cf6a6c9c\n" +
                        "b4c63f4e-fa5a-4b32-a1d3-e27db0116f7f\n" +
                        "eb862f2e-6708-4900-98ec-0519b06d78f8\n" +
                        "dafe1834-abf5-4a23-839f-fe2cab3f2adf\n" +
                        "a3430802-ec73-4f3b-a3e4-a945dd57126b\n" +
                        "9afb7963-aacc-4705-88e3-1fc443902432\n" +
                        "b67902e3-1102-47e3-b795-9c7829cc282c\n" +
                        "995aa9b8-0cfb-488b-b5f0-bb75fc6073ff\n" +
                        "3589f762-81f2-4024-921d-87b5d498e2b0\n" +
                        "58845a9e-1719-4d10-8704-1b1dd213bdd8\n" +
                        "438144ce-d38d-4d4b-8477-89e2949cfad4\n" +
                        "eb00b8c6-1e5f-482d-905f-8d8c2cda2541\n" +
                        "f7d8d01a-db4c-4dfd-880d-f4e067378ace\n" +
                        "7af8b716-4b64-4fe4-8ee7-9049432bf39d\n" +
                        "06bd2436-599e-416b-8529-9b95c8bc0d99\n" +
                        "ddb1c7cc-13bb-4f1b-b489-3be59079fcb1\n" +
                        "883a5040-f918-4eba-b774-083258bc000f\n" +
                        "92a31552-0800-4b0d-b2ba-dd74345998b9\n" +
                        "3161024f-5f9f-47fa-b994-6f1782ea40e6\n" +
                        "5e531aaf-40a9-40f5-9990-9e5c3ec64b1b\n" +
                        "1f082c07-e883-4ad2-b9b1-379e395cba2f\n" +
                        "f208499d-3019-4a3c-be32-d6d2cf2a3443\n" +
                        "039bb508-663f-4a39-a16e-bb77a61dd050\n" +
                        "76f10038-b8b3-4285-ae14-c7096c66aaac\n" +
                        "2d595f89-e05d-4fd9-91d2-a8a07755587e\n" +
                        "76ac685f-78c9-426a-a5fa-9f435130fcfa\n" +
                        "1efba9e8-21be-4a91-a0e7-642384b4db5e\n" +
                        "c5ca68fd-101d-4400-8f91-350ed73a6693\n" +
                        "a312a34c-b3a1-47d5-aff2-48e1c5a77a20\n" +
                        "44c0e80c-aa7e-43b2-8750-450176fe9ac4\n" +
                        "e66f7eac-5cfe-49e7-b455-74c7289aae8e\n" +
                        "3a79c372-47a2-4ef0-ac20-6e358faf8c8a\n" +
                        "21d0b2f9-b82c-4842-a285-c2836a6f212a\n" +
                        "20d5e054-6ce7-4aa5-85b6-6262bc373d22\n" +
                        "02886d54-c49f-460d-9231-e1a73d1d0cf9\n" +
                        "b043768a-2760-495a-9854-9b9fb4de10e0\n" +
                        "574d2113-ba4f-4dbb-8706-23dcd562bc3d\n" +
                        "2c8b336a-2413-4288-b2eb-b375934509d2\n" +
                        "b86023c3-d6b1-4c22-b6fc-18a0fd6085fb\n" +
                        "30afd723-b53c-4850-a159-48c4d9542565\n" +
                        "574a54ed-adbd-495e-96c0-781006d6d538\n" +
                        "928d3a04-6d45-4e1c-b062-826bfa1553c4\n" +
                        "5d45882c-2f06-4fcb-b64e-e20624a1038f\n" +
                        "f6ef5a5b-770e-4669-92d5-998d4b195f16\n" +
                        "94d73ef1-45cb-4174-abc3-dd0ccf9c7677\n" +
                        "86e24595-3c7d-43c7-bed1-c6ed6786054b\n" +
                        "9f4ae65f-afd5-4d7b-b65d-fe9b7e1aea21\n" +
                        "7201927a-c970-48b6-9a93-edb1fc0119d7\n" +
                        "abc8928e-d14d-4188-b2ec-1e02422935d4\n" +
                        "71e10cae-d3fa-46b1-8d3b-0bd3a6eff30b\n" +
                        "303be4e5-d6ed-46b7-8d84-d5eb2f9f00e7\n" +
                        "84dc62e2-e72a-420a-8882-1d9bbf1f4f16\n" +
                        "402bc2e5-81a6-447e-a2c9-956e97f4caba\n" +
                        "00905f2e-790a-4617-b575-15efd4eda00e\n" +
                        "77fa1811-a3c1-435e-a9ab-08823d576e12\n" +
                        "3c74bf72-5117-4287-b3cd-bf434365d39c\n" +
                        "94696a33-3e45-4112-8e37-44817e18415e\n" +
                        "e521ee4d-5356-47bd-9f5e-bd2c6eeb5998\n" +
                        "72221d72-f0c5-49c4-ba92-5aadd9c9857c\n" +
                        "efaf4577-b603-4110-a419-a6005d04fc47\n" +
                        "d7eb60cc-ade7-4dc0-800e-95fdf1dc82d4\n" +
                        "9b5c7caf-a90b-4901-8fbb-e7134863c957\n" +
                        "9f8daea5-f7a9-4ee0-85a6-92d56fac6954\n" +
                        "efb8bf58-4c60-4ee4-8656-da5b02c72e65\n" +
                        "7c443c19-463b-4140-baeb-3aa09145961e\n" +
                        "e162f528-0b09-496f-bf53-7ed2c58f6896\n" +
                        "f4701990-6bd3-4e6b-91ca-9cec21184a2a\n" +
                        "00675b00-19ac-4ff5-a5b2-9fc3cdde982b\n" +
                        "fac962a3-7fa0-4c57-8296-88f771450016\n" +
                        "001b287c-a8bf-4df3-bd9d-261444d830f5\n" +
                        "7f2e212d-9158-4eb4-ae0a-d41816102446\n" +
                        "bb7311c3-83ae-4730-b195-d044f827bebb\n" +
                        "49819664-8db1-4734-9da6-53ab235e68f1\n" +
                        "5955115a-a3d5-4d6a-b53e-c03cd7d8b502\n" +
                        "e8997036-a2fb-4b58-ab28-06a8e9a32789\n" +
                        "142a076d-d26d-4ce9-8112-c08c531c8825\n" +
                        "2f951627-8739-4b99-b8eb-30c5e0471246\n" +
                        "033cb254-2988-4f69-ba68-733eb75aabb0\n" +
                        "6916c61d-f269-41c1-9a64-d8de3696c382\n" +
                        "7c3efb7d-f454-4389-a980-dff2c37ed7d6\n" +
                        "473bc194-19eb-4e74-aa1c-b495d7664876\n" +
                        "0f32ee55-d128-44b7-bf1a-75339a7b828b\n" +
                        "bc268913-6125-41b7-ac6a-0d7dd5f8bf8b\n" +
                        "4cd5ef57-cc35-4cbe-a5cd-7c2d3f96267f\n" +
                        "7b9916ec-2506-4629-babf-72042a05e03f\n" +
                        "db9b3ae3-d537-4c3f-8611-a1af3c576cd3\n" +
                        "d6590c50-9ff6-455c-84a6-efc5a98563db\n" +
                        "30f807f8-123e-40f8-bac1-3cbcd780b593\n" +
                        "ce39ddf9-0816-4e16-bac7-c86d251764b2\n" +
                        "9f94f0ce-72e7-48d8-a45f-f8424b781c78\n" +
                        "302dbffa-4786-4120-8dad-91e7e11e2062\n" +
                        "9f8d8f43-a8c4-4498-a8a7-0bff1e396193\n" +
                        "b382dfc8-6833-487f-b612-c370fcc55e1d\n" +
                        "2c9bbea7-2a8d-4641-b8f8-22498c491fe1\n" +
                        "c9627e84-bcd3-4676-86b5-a7b26e70e8b4\n" +
                        "80f7004e-b8b0-42a8-b210-7922f8c737a8\n" +
                        "7d8b8f82-5b40-436e-8faa-86a939e36640\n" +
                        "c0e3670b-12c3-4184-a853-7ff0e59ad159\n" +
                        "5dc36bdc-457b-4b24-b836-5f359ef7cc0e\n" +
                        "ab0cb68a-e2b9-438c-9dcb-c272bb7324a3\n" +
                        "464dbef2-50dd-4d47-af78-52c36331dd8d\n" +
                        "3602fe37-6511-4f64-a8f3-6f74c703f665\n" +
                        "361f6246-9f70-4925-a7db-d0970d5a7920\n" +
                        "da3e016e-7bde-486e-bfad-13c25795ce00\n" +
                        "d3aaf4cc-e287-4799-a08a-cd29c757617a\n" +
                        "6b197c2d-39c3-45c8-a63b-3fd4b5d0c058\n" +
                        "4fefda52-0273-4bae-9bd7-a298da410b69\n" +
                        "dd8cb009-926d-4aff-913f-0e212e319ddd\n" +
                        "d638b0bf-0208-46e5-ac04-4e39633e96cf\n" +
                        "0dd9001c-f6ae-4432-afae-4231fe08adbc\n" +
                        "dbebf2ce-9c11-44e3-ba1d-05ad82e62a54\n" +
                        "a89a5d2a-7b06-4d6b-8365-b21d9bf8ffe5\n" +
                        "8afaafb3-e330-4a9b-a6be-6fa9ba83323d\n" +
                        "660489d6-c45e-4a40-8964-66892fcb98b1\n" +
                        "b1df65ef-273f-4493-a980-f8b7e1717d19\n" +
                        "1ec05aa9-26bf-4652-86bd-e56c17445e5a\n" +
                        "28fa1b70-416a-4e92-9739-a781ec39280f\n" +
                        "16e54f14-1b54-4ab3-afaa-01b184701550\n" +
                        "a56e0039-949d-4507-9c9c-83f286a02377\n" +
                        "adea4a31-ab41-460a-84d6-c1905b7179f5\n" +
                        "de8bad26-10b1-481e-8a82-3399e56399b1\n" +
                        "ecf0b678-49e8-4ddd-99e2-853013b2d6ec\n" +
                        "3a64bdfb-d2f0-48cc-bf56-9858ab4027c2\n" +
                        "27af4835-20aa-4c53-97c5-aa57326fcecf\n" +
                        "958f6985-c405-4070-bdf8-a083bbe679e4\n" +
                        "4803019a-fb9b-4e99-821e-d21125daea58\n" +
                        "bf5d6199-a886-4094-926b-d99d0f20f7f0\n" +
                        "6543eece-a2ad-4db2-b8d1-665cd0e54c1c\n" +
                        "b4153d8f-7f4e-4c95-992f-c1410c7dc154\n" +
                        "3717cd5f-17f9-4d88-93d4-6f90400fe2ca\n" +
                        "20086b42-a01b-49d8-ac2e-1027b360baa9\n" +
                        "410b4679-15e7-4dcd-a89d-e838d151dd70\n" +
                        "6b6fede8-fac3-4c8b-9044-6950def9067e\n" +
                        "26697d3c-4711-4253-b86b-041047e04d5f\n" +
                        "d0c92e5d-62d1-430f-a37f-c7a5cd5df3d7\n" +
                        "dfe042f3-80e5-49a0-857d-6a1c2427e329\n" +
                        "4d36bb96-73e4-4e23-a190-1a897f81665e\n" +
                        "dbe961cf-75a0-4d0e-8be0-1820551a150c\n" +
                        "71a0a725-03af-425f-999c-a28a1995b70b\n" +
                        "3480bab6-6554-4d33-81d4-0aae1adae6da\n" +
                        "b02db2bf-7490-4d92-882e-4a686b2af59b\n" +
                        "6c041a37-3cd8-4d7a-9727-26194f1ea0c4\n" +
                        "cb92a448-be98-4274-8725-f9f6816304ce\n" +
                        "b5006060-dd73-4aa8-b278-b66b50b0f85c\n" +
                        "950ff202-717d-4754-8f90-960b5fd353b9\n" +
                        "a8fe471f-e0bc-4557-9252-3eae95e65341\n" +
                        "ed453aaf-5590-4220-95e6-ce95127721d7\n" +
                        "d5cb13fa-f71e-43e0-9bb5-113cfafffd30\n" +
                        "69760486-79a4-4099-9c2f-9c1ddf504f58\n" +
                        "7f617f2e-e66d-42cd-9aee-cc878f1acfea\n" +
                        "e5a6f673-35d1-4fc4-b552-6a239ff56dec\n" +
                        "31351018-60be-4b6d-b1d3-75e6999e566f\n" +
                        "46102dd9-9c9c-49ac-83c8-9233f6c98f92\n" +
                        "6319a4df-1cbd-4b3b-8cf2-2941b28be391\n" +
                        "a5eacb01-4b57-4e74-9553-948a7c0fd0a3\n" +
                        "a05ae872-31ec-4869-a4b2-6c477c5c0ad3\n" +
                        "85c66f99-18b1-4190-93a8-572c5c9a06bf\n" +
                        "1a948860-44b5-4fcf-b00b-5e6df4f80dae\n" +
                        "9782fbd8-fa80-4197-8a4d-57b57ce96027\n" +
                        "e6878d05-6dd2-4fd8-ab17-c4c9b064091d\n" +
                        "5f6432d3-37ad-4f29-a2d8-2d84c04eaa5d\n" +
                        "adf6c8f9-94bc-4e7b-98fc-6d0adfdd81da\n" +
                        "5353051a-c18d-4d89-83cb-913c6c5bedb3\n" +
                        "1b58f6c4-a646-40e6-901d-6c6dc028864d\n" +
                        "bcc2b0b4-4f7d-4ccc-b60a-8dda75cafd0c\n" +
                        "d7cee683-0101-4da4-aea1-e10e0d9577db\n" +
                        "f8b1ff10-ac98-481c-90a5-ad6f87b0678a\n" +
                        "0c6fc4a1-6fa2-4a2d-808a-c8379e1d4da4\n" +
                        "8907bd2c-35a5-4186-9c73-9579e3a85a85\n" +
                        "f05a23ac-4472-4350-bc5b-7fc32f698e67\n" +
                        "bdcbcc1c-6457-40e6-bdf1-fd3ab1c25cd2\n" +
                        "eb83456d-a72c-4373-896e-d73411379c22\n" +
                        "20abce71-f4e0-45e7-a5cd-1ba4a3b7fc53\n" +
                        "e3a373df-8177-4269-9d11-20f1a35e9209\n" +
                        "fbececd7-cdea-4897-9adb-7df9a80e5bd1\n" +
                        "ff86b37a-9581-4e4e-9a6e-952b513db22a\n" +
                        "ce9eed93-19de-425c-9cd1-7655ca9f326b\n" +
                        "3bb040c8-6d65-4bb1-a6e4-2507ce04eab9\n" +
                        "7aa5d99f-c362-4ea9-be63-942fbd3a99f3\n" +
                        "2154a322-0369-4131-b2d9-a4ad97236cfd\n" +
                        "f783e550-c953-4b3a-95d6-d0dd3116edee\n" +
                        "64b1073b-905b-4c76-8c2f-c521b49d5d27\n" +
                        "8deb6f48-f2a2-4ce9-924a-55a1d69e4ee7\n" +
                        "dd16c414-b59a-4cac-80d4-47119a49a107\n" +
                        "1d635cbf-6c27-48bb-9658-e8a6554cf57e\n" +
                        "862a0a59-c285-4590-8826-fae830729e2f\n" +
                        "f0178873-9dda-40fa-ac17-0bc80105dce5\n" +
                        "748a69bc-8ac0-4fcf-b475-4ab996b66d59\n" +
                        "95e55403-2572-4192-b0eb-6dd1df7d67b4\n" +
                        "15ea46ea-cbe6-47e8-9602-76ab6a90241b\n" +
                        "ac4d1312-617e-46d3-9cd4-2427a1a621fc\n" +
                        "e488b66a-5a7c-4e71-9de7-b9e990ed04aa\n" +
                        "0a29b120-8f98-4473-b222-5fc6d658c8ff\n" +
                        "48a1ee5f-816f-4a79-b1a3-6c151cc6b574\n" +
                        "d93957a9-fccb-42fe-81bc-eeea08da454b\n" +
                        "d7ee977b-7c3f-4039-9426-f56461515a65\n" +
                        "662bdbab-154e-4644-a4a8-cb79ac9d7115\n" +
                        "adef377b-b583-42ae-8875-ee37ed115e39\n" +
                        "8389b22e-7cc7-42f5-86e1-ed1e10504f10\n" +
                        "760e876c-4e6a-4797-b5f0-705309eae8c7\n" +
                        "22c2004c-8771-41b8-ad35-96c0d7442936\n" +
                        "185482b4-89a4-4865-835b-62750349288c\n" +
                        "cfb7215d-0caa-4260-b314-4321b8dd47f5\n" +
                        "f976e9e0-d8e1-4b53-812c-68d15fc4d9ce\n" +
                        "aeb2250f-f5ec-4b58-9bd0-f18ec5c184ea\n" +
                        "4eaa843f-571c-4408-b9fe-dbad38529c7b\n" +
                        "95d2f0be-2298-4157-ae9b-42cc06405368\n" +
                        "2820bbb2-3af8-4390-909f-10f75d31d532\n" +
                        "50cd6570-3990-4610-9bab-67524b50dddc\n" +
                        "c5d5e961-b323-4d35-a660-780a86f646d1\n" +
                        "a02d2a81-64e8-4ecc-9bd4-bf14bf314f93\n" +
                        "d8d0fe6b-e0a5-4060-9170-790cc49c3a09\n" +
                        "66049de5-019b-40bb-b803-0c236c689bc2\n" +
                        "38244ef2-7df7-4e8f-a533-c81b87a2e8c8\n" +
                        "4723842c-5330-4b13-a45a-992a3d1f3f04\n" +
                        "22859838-be06-44dd-b622-0a42868a2780\n" +
                        "de3bcb20-c8b4-49e2-9c39-53cbce6b36c7\n" +
                        "044fdaa7-61fe-45b1-bd7c-d80cfb8a7eac\n" +
                        "0eb68e8b-185c-47b9-97ff-dcb253a4a7bc\n" +
                        "fb207c5b-9773-4505-98c0-348e01bae5c9\n" +
                        "4d1b8c02-a76c-4944-a80c-0f876afd946b\n" +
                        "f753f405-6bd4-44f2-8be6-c0b11108c05c\n" +
                        "5226732c-2dae-41d2-9ef8-8d17370f5d02\n" +
                        "131d49e3-67b4-4a89-af22-eccad56079e6\n" +
                        "b0726af7-a81c-4eae-b63b-3a1830619582\n" +
                        "70734b86-c019-4e73-835a-6df4f23430e2\n" +
                        "dbed299b-b2e4-4644-9ec4-f1c2e25e4392\n" +
                        "e739e187-908d-4d54-8b8a-88533b284d30\n" +
                        "198a4a36-7a39-490a-b0fb-ee9a82fe8175\n" +
                        "e9eeedcc-4ba0-4ed4-a02c-0455b598dd15\n" +
                        "bda567ac-f14c-495b-b92e-99e90e4ac081\n" +
                        "d717d581-aa95-4bcf-8f6b-3fcd6fdd7682\n" +
                        "2530a9ea-382f-485b-8361-dbff806ae448\n" +
                        "6afee81b-a53e-40f6-b3ea-032f5e93539a\n" +
                        "e085590b-f52c-4fca-b4c5-a183453abd0a\n" +
                        "21ebf453-c3fc-425e-bbd8-2ec329f3c12a\n" +
                        "0039d523-3383-4e4e-87db-9f0e0c4df379\n" +
                        "d10c43be-a46f-45ce-917a-7433563e1332\n" +
                        "a06d9e93-065a-4dee-9053-798435a27fc1\n" +
                        "df376cfe-b8b3-433b-802b-2c9d6e92ba82\n" +
                        "0a1d1d94-5b00-4273-a38f-4fa2110c8995\n" +
                        "b8aa5d8f-76f0-4c7c-a3e7-7c75d7f9f27f\n" +
                        "8611d65e-de87-4582-9f73-a9868313dc35\n" +
                        "37470ae5-f43f-414d-baa6-f7800dc114f4\n" +
                        "6ce85154-8e33-4fc2-93ed-81bfbe84dc33\n" +
                        "389f198d-5484-41c0-821f-5048d4fa80cb\n" +
                        "ce15054d-3bab-4975-ab82-4c73542f732b\n" +
                        "99cd3a23-23fb-47f8-a4b1-9cb66372fe1d\n" +
                        "dbf1fe6a-12e9-493b-8807-3ea65c839fd2\n" +
                        "836f02cd-b705-4f7c-9a36-80e68b629d66\n" +
                        "548acc1a-38d8-4b51-bb52-753e0c549fe5\n" +
                        "a5118d51-9bc3-456c-83f4-0afce8243e89\n" +
                        "c5361dac-3cf7-496e-a2e3-cb8b12e72de2\n" +
                        "5c6b02d4-baae-4106-908a-05b7a593c57a\n" +
                        "063a1c88-1db8-4345-b82a-f424f5e3861d\n" +
                        "0c8a3e14-a104-4b70-8288-8bcdd9a31385\n" +
                        "2c51bcad-45e9-4827-9567-2ea221ffb417\n" +
                        "be2ef40f-72a2-4b61-aeef-8ea7b545c1d4\n" +
                        "80383896-eb48-4497-be47-ea48b8dde044\n" +
                        "19ebc343-fe0b-491a-9c3c-5bf22e98a9e0\n" +
                        "75a03ca4-9a51-41fd-8c30-ef71c80074fa\n" +
                        "2860ab05-f7fe-4aed-b67a-5862624e3896\n" +
                        "63e4f175-e3f5-48d7-92ca-f197921ff441\n" +
                        "0adf47b4-5e1f-46bb-9b00-c4a1bf22f383\n" +
                        "7a9f1be4-27eb-4667-889f-b70e856dc580\n" +
                        "275fa405-70d4-4624-8a80-177b0b9c8096\n" +
                        "348aa3b4-759c-478d-b08c-8ccd642e0b87\n" +
                        "ca0f38c1-667b-44e1-bc89-0c5a46c3c762\n" +
                        "cdbb33e2-56bd-49a8-baf1-c2ec02f27274\n" +
                        "fe885528-c40b-4b2c-a66e-81d924c1531f\n" +
                        "2bd35420-4b3d-4d59-b0ae-f7887caf17af\n" +
                        "ac49c13d-42cf-46f6-bd33-19561032229f\n" +
                        "74060f2f-d26a-4933-9a6c-de50c7fc2a67\n" +
                        "53735668-1719-4f87-bd02-4df511b9cceb\n" +
                        "2e77f2dd-b444-4ec3-b789-c4fdecd071b4\n" +
                        "fd765fb1-d845-40fb-a337-d8cdf4192762\n" +
                        "dba7bd7c-1d2f-45a4-abe2-99f8987d2738\n" +
                        "250911c0-f303-43d8-9a55-d2195784dcf3\n" +
                        "c86c6d4f-148f-4d5b-9cd6-5afe731730ba\n" +
                        "6bfaf8a0-c30f-4e4c-b75e-67d5bfb199aa\n" +
                        "e562f577-6cf8-4ad5-ba4b-9588b4d607af\n" +
                        "cccd2363-af58-451c-93c4-45f186608017\n" +
                        "bd6f947b-9491-49b2-a712-5e4489365d71\n" +
                        "80e8d096-4941-40ef-9e7b-3bbb6a224fd3\n" +
                        "8b210d99-52e7-49e7-91eb-0a77caa7882e\n" +
                        "8969baa6-4a44-42e6-a076-f74f377a4ca5\n" +
                        "e976949f-4978-4559-8463-df901cf3f73f\n" +
                        "bd94b9da-7f4e-4166-8e46-f162948948b5\n" +
                        "3ad7cf5a-25bb-4a6b-88fb-23ee8dfdb7f4\n" +
                        "c9acfac6-3512-4223-800b-253a0d180a10\n" +
                        "7eb94231-5453-408d-8032-9613148337da\n" +
                        "f97f2400-b973-48ca-9906-8d1d03e4b8bb\n" +
                        "697c10e7-88cf-49e0-8a22-65358d214e16\n" +
                        "cd7d838f-cef0-4d25-8a88-e4cb4c6b2514\n" +
                        "d4aa055f-8018-4995-b7bc-e855e9d7932f\n" +
                        "86c0ec2f-6fa9-4cbc-bf72-478912367b65\n" +
                        "e564e2f4-8899-486d-a324-5862b925f682\n" +
                        "954e6ccc-af5d-4410-bc43-87aaedd30d50\n" +
                        "7cd8912d-98bb-499c-ad18-01bcc25f1ee2\n" +
                        "77ea7c18-4fe1-4727-8966-46d4e01bc45b\n" +
                        "eafba2bb-06a0-4ac3-97f9-959bf8879d0e\n" +
                        "2cec82aa-b7f0-4f16-918e-c89a47533c68\n" +
                        "a43a97ee-96db-493e-bcca-93153a9af399\n" +
                        "87c3d96f-8706-4879-9d93-8c9038591a0d\n" +
                        "e21bb05e-c7c6-4085-9d0d-a6ba813227c8\n" +
                        "ac9e1c34-49ac-4c9c-a4b8-c21e9a238a71\n" +
                        "05f9f6cb-191f-4df5-8419-f87a3a71dd12\n" +
                        "993743b0-d319-4bb2-bf03-bb0dfe18fcbc\n" +
                        "3df92d22-859b-4269-93f4-b94ad31e2123\n" +
                        "3c51ddb5-3766-4b2b-979d-3bcc67f784f3\n" +
                        "5aa81cd0-6dfd-4395-bc52-3032c62c343a\n" +
                        "336a6fa9-a39b-4db4-a4d6-bc263030d11a\n" +
                        "a0eff06c-a2d5-4d62-bfae-4a95d291e3df\n" +
                        "9ac33a1b-6efe-4972-a4bf-b5baba91ee64\n" +
                        "c1d09da5-b2a8-49ca-9023-c224cf69eb01\n" +
                        "4b982107-74e4-4bd0-971d-4745b89c81df\n" +
                        "51c34e72-6cc6-42f3-9e16-4a65138aceab\n" +
                        "730913a5-5aca-426b-9451-05b5031369e7\n" +
                        "c57276b5-1e63-4b15-abd9-a8918a3abdf9\n" +
                        "e933fcf6-3cc2-4243-b2f9-eb2758a5f603";
    }

    public static String generateLogLine(int seconds, EventType eventType, String uuid) {
        return "{\n" +
                "  \"timestamp\": \"2016-08-25T16:34:" + seconds + "\",\n" +
                "  \"type\": \"" + eventType.toString() + "\",\n" +
                "  \"worldID\": \"Main\",\n" +
                "  \"userID\": \"" + uuid + "\",\n" +
                "  \"level\": \"INFO\",\n" +
                "  \"year\": \"2016\",\n" +
                "  \"month\": \"08\",\n" +
                "  \"day\": \"25\",\n" +
                "  \"hour\": \"16\",\n" +
                "  \"minute\": \"" + seconds / 60 + "\",\n" +
                "  \"seconds\": \"" + seconds % 60 + "\"\n" +
                "}";
    }*/

}
