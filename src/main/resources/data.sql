-- カテゴリテーブル --
-- カテゴリデータの挿入
INSERT IGNORE INTO categories (name)
VALUES
('カフェ'),
('鍋料理'),
('手羽先'),
('カレー'),
('カツ'),
('うなぎ'),
('和食'),
('パスタ'),
('ピザ'),
('ラーメン'),
('魚介・海鮮料理'),
('ステーキ'),
('スイーツ'),
('うどん'),
('洋食');

-- 会員情報テーブル --
-- テスト用会員データ --
INSERT IGNORE INTO members (name, furigana, postal_code, address, phone_number, email, password, role_id, enabled, status)
VALUES
('侍太郎', 'サムライタロウ', '123-4567', '名古屋市中区栄1-1-1', '052-123-4567', 'test@example.com', '$2a$08$Lqz/1gfIm4iIYigKR3Dz3elVe5cLUbUwVztsXs2xhILTDwAFAT7SS', '1', '1', 'FREE'),
('武士花子', 'ブシハナコ', '123-4568', '名古屋市中区栄2-2-2', '052-234-5678', 'bushi@example.com', '$2a$08$c6x3bJZ0awA0/tmEqyGVEu6UurClvH50QPTwkmWGtQa9ETxjHN8SS', '2', '1', 'PAID');

-- ロール情報テーブル --
INSERT IGNORE INTO roles (name)
VALUES
('ROLE_FREE'),
('ROLE_PAID'),
('ROLE_ADMIN');

-- 会員ロール情報テーブル --
INSERT IGNORE INTO member_roles (member_id, role_id)
VALUES
((SELECT member_id FROM members WHERE email='test@example.com'), (SELECT role_id FROM roles WHERE name='ROLE_FREE')),
((SELECT member_id FROM members WHERE email='bushi@example.com'), (SELECT role_id FROM roles WHERE name='ROLE_PAID'));

-- 管理者情報テーブル --
INSERT IGNORE INTO admins (email, password)
VALUES
('admin@example.com', '$2a$08$Zl0MMaNbANtJ31l6dAjr5.DbVmVwk./QU0.xnDai3YN/1oaOfQ3da');

-- 店舗情報テーブル --
-- カフェの店舗情報
INSERT IGNORE INTO stores (category_id, store_name, image_filename, rating, description, price, postal_code, address, phone_number, opening_hours, closed_days)
VALUES 
(1, 'リラックスカフェ', 'cafe01.jpg', 4, 'おしゃれなカフェでリラックスできます', 1000, '123-4568', '名古屋市中区栄1-1-2', '052-123-4568', '09:00 - 21:00', '月曜日'),
(1, 'コージーカフェ', 'cafe02.jpg', 5, '居心地の良いカフェで素敵な時間を', 1500, '123-4569', '名古屋市中区栄1-1-3', '052-123-4569', '08:00 - 20:00', '火曜日'),
(1, 'レトロカフェ', 'cafe03.jpg', 3, 'レトロな雰囲気のカフェでコーヒーを楽しめます', 800, '123-4570', '名古屋市中区栄1-1-4', '052-123-4570', '10:00 - 22:00', '水曜日'),
(1, 'のんびりカフェ', 'cafe04.jpg', 4, '落ち着いた雰囲気のカフェでのんびりできます', 900, '123-4571', '名古屋市中区栄1-1-5', '052-123-4571', '09:00 - 23:00', '木曜日'),
(1, 'モダンカフェ', 'cafe05.jpg', 5, 'モダンなカフェでおいしいコーヒーを楽しめます', 1200, '123-4572', '名古屋市中区栄1-1-6', '052-123-4572', '07:00 - 20:00', '金曜日');
-- 鍋料理の店舗情報
INSERT IGNORE INTO stores (category_id, store_name, image_filename, rating, description, price, postal_code, address, phone_number, opening_hours, closed_days)
VALUES 
(2, '豆腐鍋の隠れ家', 'casserole01.jpg', 4, '美味しい豆腐鍋', 1200, '123-4568', '名古屋市中区栄1-1-2', '052-123-4568', '11:00 - 22:00', '水曜日'),
(2, '火鍋専門店マーラー', 'casserole02.jpg', 5, '辛味のある麻辣火鍋', 1500, '123-4569', '名古屋市中区栄1-1-3', '052-123-4569', '10:00 - 23:00', '月曜日'),
(2, 'すき焼き庵', 'casserole03.jpg', 4, '伝統的なすき焼き鍋', 1800, '123-4570', '名古屋市中区栄1-1-4', '052-123-4570', '12:00 - 21:00', '火曜日'),
(2, 'キムチ鍋の楽園', 'casserole04.jpg', 5, '具沢山のキムチ鍋', 2000, '123-4571', '名古屋市中区栄1-1-5', '052-123-4571', '11:00 - 22:00', '木曜日'),
(2, '日本の鍋屋さん', 'casserole05.jpg', 3, '伝統的な日本の鍋料理', 1300, '123-4572', '名古屋市中区栄1-1-6', '052-123-4572', '11:00 - 20:00', '金曜日');
-- 手羽先の店舗情報
INSERT IGNORE INTO stores (category_id, store_name, image_filename, rating, description, price, postal_code, address, phone_number, opening_hours, closed_days)
VALUES 
(3, '手羽先マスター', 'chickenwings01.jpg', 4, '名古屋の名物手羽先を提供するお店です。秘伝のタレが絶品です。', 1500, '789-1234', '名古屋市中区栄2-2-2', '052-345-6789', '17:00 - 23:00', '月曜日'),
(3, 'クリスピー手羽先', 'chickenwings02.jpg', 5, 'サクサクの衣が特徴の手羽先が楽しめるお店。ビールとの相性抜群です。', 1800, '789-1235', '名古屋市中区栄3-3-3', '052-345-6790', '17:00 - 23:00', '火曜日'),
(3, '手羽先バー', 'chickenwings03.jpg', 3, 'リーズナブルな価格で手羽先が楽しめます。味付けはやや濃いめ。', 1200, '789-1236', '名古屋市中区栄4-4-4', '052-345-6791', '17:00 - 23:00', '水曜日'),
(3, '手羽先の焼き場', 'chickenwings04.jpg', 4, '香ばしく焼き上げた手羽先が自慢のお店。ちょっと辛めの味付けが特徴です。', 1600, '789-1237', '名古屋市中区栄5-5-5', '052-345-6792', '17:00 - 23:00', '木曜日'),
(3, '手羽先デライト', 'chickenwings05.jpg', 5, 'ジューシーな手羽先を提供するお店。甘辛のタレが絶品です。', 2000, '789-1238', '名古屋市中区栄6-6-6', '052-345-6793', '17:00 - 23:00', '金曜日');
-- カレーの店舗情報
INSERT IGNORE INTO stores (category_id, store_name, image_filename, rating, description, price, postal_code, address, phone_number, opening_hours, closed_days)
VALUES
(4, 'インドカレーハウス', 'curry01.jpg', 4, 'スパイスの効いた本格的なインドカレー。', 1200, '123-4591', '名古屋市中区カレー1-1-1', '052-123-4591', '10:00 - 22:00', '火曜日'),
(4, 'トロピカルカレーカフェ', 'curry02.jpg', 5, 'ココナッツミルクを使ったクリーミーなカレー。', 1300, '123-4592', '名古屋市中区カレー1-1-2', '052-123-4592', '11:00 - 23:00', '水曜日'),
(4, 'ビーナッツカレーキッチン', 'curry03.jpg', 4, '香ばしいピーナッツが特徴のカレー。', 1100, '123-4593', '名古屋市中区カレー1-1-3', '052-123-4593', '12:00 - 21:00', '木曜日'),
(4, 'バターチキンスペシャリティ', 'curry04.jpg', 5, '伝統的なバターチキンカレー。', 1500, '123-4594', '名古屋市中区カレー1-1-4', '052-123-4594', '10:00 - 22:00', '金曜日'),
(4, 'スパイシービーフカレー', 'curry05.jpg', 3, '辛味が強烈なビーフカレー。', 1400, '123-4595', '名古屋市中区カレー1-1-5', '052-123-4595', '09:00 - 20:00', '土曜日');
-- カツの店舗情報
INSERT IGNORE INTO stores (category_id, store_name, image_filename, rating, description, price, postal_code, address, phone_number, opening_hours, closed_days)
VALUES 
(5, 'カツカレ王国', 'cutlet01.jpg', 5, '特製ソースが自慢のカツレツ。サクサクの衣とジューシーな肉が楽しめます。', 1200, '123-4580', '名古屋市中区栄3-1-1', '052-123-4580', '11:00 - 22:00', '日曜日'),
(5, 'サクサクカツカレ', 'cutlet02.jpg', 4, '風味豊かなパン粉を使用したカツレツが人気のお店。', 1100, '123-4581', '名古屋市中区栄3-1-2', '052-123-4581', '11:00 - 21:00', '月曜日'),
(5, 'コロッケカツコーナー', 'cutlet03.jpg', 3, 'コロッケカツが名物のお店。お手頃価格で楽しめます。', 900, '123-4582', '名古屋市中区栄3-1-3', '052-123-4582', '11:00 - 20:00', '火曜日'),
(5, '自家製ソースのカツカレ', 'cutlet04.jpg', 4, '自家製ソースが特徴のカツレツが自慢。ボリューム満点。', 1300, '123-4583', '名古屋市中区栄3-1-4', '052-123-4583', '11:00 - 22:00', '水曜日'),
(5, 'カツレアアーティスト', 'cutlet05.jpg', 5, 'シンプルながらも絶品のカツレツ。おしゃれな雰囲気のお店。', 1500, '123-4584', '名古屋市中区栄3-1-5', '052-123-4584', '11:00 - 23:00', '木曜日');
-- 鰻の店舗情報の挿入
INSERT IGNORE INTO stores (category_id, store_name, image_filename, rating, description, price, postal_code, address, phone_number, opening_hours, closed_days)
VALUES 
(6, '炭火焼うなぎ屋', 'eel01.jpg', 5, '伝統的な炭火焼きで提供される美味しいうなぎです。', 3000, '460-0002', '名古屋市中区栄1-2-3', '052-123-4561', '11:00 - 21:00', '水曜日'),
(6, '秘伝のタレうなぎ', 'eel02.jpg', 4, '秘伝のタレで焼かれたうなぎが自慢のお店です。', 2800, '460-0002', '名古屋市中区栄1-2-4', '052-123-4562', '11:30 - 20:30', '木曜日'),
(6, 'うなぎの里', 'eel03.jpg', 3, '家庭的な雰囲気で楽しめるうなぎ料理店です。', 2500, '460-0002', '名古屋市中区栄1-2-5', '052-123-4563', '12:00 - 21:00', '火曜日');
-- 和食の店舗情報
INSERT IGNORE INTO stores (category_id, store_name, image_filename, rating, description, price, postal_code, address, phone_number, opening_hours, closed_days)
VALUES 
(7, '味噌汁マニア', 'japanesefood01.jpg', 4, '具沢山の味噌汁が楽しめる専門店。地元の食材を使ったこだわりの味噌汁が人気です。', 800, '460-0008', '名古屋市中区栄1-1-1', '052-111-1111', '10:00 - 22:00', '水曜日'),
(7, '寿司天国', 'japanesefood02.jpg', 5, '新鮮なネタが自慢の寿司屋。季節の魚介を使った握りや巻き寿司が楽しめます。', 1500, '460-0008', '名古屋市中区栄2-2-2', '052-222-2222', '11:00 - 23:00', '火曜日'),
(7, '海鮮丼センター', 'japanesefood03.jpg', 4, '新鮮な海鮮丼が味わえる店。海の幸をふんだんに使ったボリューム満点の丼が楽しめます。', 1300, '460-0008', '名古屋市中区栄3-3-3', '052-333-3333', '11:30 - 21:00', '月曜日'),
(7, '焼き魚デリ', 'japanesefood04.jpg', 5, '香ばしく焼き上げた魚が絶品の和食店。焼き魚定食が人気です。', 1200, '460-0008', '名古屋市中区栄4-4-4', '052-444-4444', '12:00 - 22:00', '木曜日'),
(7, '蕎麦屋さん', 'japanesefood05.jpg', 3, 'そばが自慢の和食店。風味豊かなそばと手作りのつゆが特徴です。', 1000, '460-0008', '名古屋市中区栄5-5-5', '052-555-5555', '11:00 - 20:00', '金曜日');
-- パスタの店舗情報
INSERT IGNORE INTO stores (category_id, store_name, image_filename, rating, description, price, postal_code, address, phone_number, opening_hours, closed_days)
VALUES
(8, '海老パスタの家', 'pasta01.jpg', 4, '新鮮な海老がたっぷり入った海老トマトクリームパスタ。', 1800, '123-4567', '名古屋市中区パスタ1-1-1', '052-123-4567', '11:00 - 22:00', '火曜日'),
(8, 'ジェノベーゼの達人', 'pasta02.jpg', 5, 'ヘルシーなジェノベーゼパスタ。新鮮なバジルが香る。', 1500, '123-4568', '名古屋市中区パスタ1-1-2', '052-123-4568', '10:00 - 21:00', '水曜日'),
(8, 'シンプルスパゲッティ', 'pasta03.jpg', 4, 'トマトとバジルのシンプルなスパゲッティ。', 1200, '123-4569', '名古屋市中区パスタ1-1-3', '052-123-4569', '11:30 - 23:00', '木曜日'),
(8, 'カルボナーラキッチン', 'pasta04.jpg', 3, 'クリーミーなカルボナーラ。濃厚なチーズソースが特徴。', 1300, '123-4570', '名古屋市中区パスタ1-1-4', '052-123-4570', '12:00 - 21:30', '金曜日'),
(8, 'ボロネーゼワールド', 'pasta05.jpg', 5, 'トマトソースたっぷりのボロネーゼ。', 1400, '123-4571', '名古屋市中区パスタ1-1-5', '052-123-4571', '10:00 - 20:00', '月曜日');
-- ピザの店舗情報
INSERT IGNORE INTO stores (category_id, store_name, image_filename, rating, description, price, postal_code, address, phone_number, opening_hours, closed_days)
VALUES 
(9, 'ナポリの味', 'pizza01.jpg', 5, '本格的なナポリピッツァが楽しめるお店です。', 1800, '123-4561', '名古屋市中区ピザ1-1-1', '052-123-4561', '11:00 - 22:00', '火曜日'),
(9, 'カスタムピザスタジオ', 'pizza02.jpg', 4, '様々なトッピングが選べるカスタムピザが人気です。', 1600, '123-4562', '名古屋市中区ピザ1-1-2', '052-123-4562', '12:00 - 23:00', '水曜日'),
(9, '薪窯ピザバー', 'pizza03.jpg', 5, '薪窯で焼いたピザが自慢のお店です。', 2000, '123-4563', '名古屋市中区ピザ1-1-3', '052-123-4563', '10:00 - 21:00', '木曜日'),
(9, 'ピザエクスプレス', 'pizza04.jpg', 3, 'テイクアウト専門のピザショップ。', 1200, '123-4564', '名古屋市中区ピザ1-1-4', '052-123-4564', '11:00 - 20:00', '金曜日'),
(9, 'チーズピザの楽園', 'pizza05.jpg', 4, 'チーズたっぷりのピザが特徴のお店です。', 1500, '123-4565', '名古屋市中区ピザ1-1-5', '052-123-4565', '11:30 - 22:00', '土曜日');
-- ラーメンの店舗情報
INSERT IGNORE INTO stores (category_id, store_name, image_filename, rating, description, price, postal_code, address, phone_number, opening_hours, closed_days)
VALUES 
(10, 'ラーメンチャーシューバー', 'ramen01.jpg', 5, '濃厚なスープと柔らかいチャーシューが自慢のラーメン店です。', 800, '460-0002', '名古屋市中区錦1-2-3', '052-123-4576', '11:00 - 22:00', '火曜日'),
(10, '辛味噌ラーメンスペシャル', 'ramen02.jpg', 4, '特製の辛味噌ラーメンが人気のお店です。', 850, '460-0003', '名古屋市中区錦1-2-4', '052-123-4577', '11:30 - 21:30', '水曜日'),
(10, 'ラーメンホーム', 'ramen03.jpg', 3, '家庭的な雰囲気で楽しめるラーメン店です。', 700, '460-0004', '名古屋市中区錦1-2-5', '052-123-4578', '12:00 - 21:00', '木曜日'),
(10, '海鮮塩ラーメン', 'ramen04.jpg', 5, '魚介系の出汁が効いた塩ラーメンが自慢のお店です。', 900, '460-0005', '名古屋市中区錦1-2-6', '052-123-4579', '11:00 - 23:00', '金曜日'),
(10, '担々麺専門店', 'ramen05.jpg', 4, 'ピリ辛の担々麺が人気のラーメン店です。', 850, '460-0006', '名古屋市中区錦1-2-7', '052-123-4580', '11:00 - 22:00', '土曜日');
-- 魚介・海鮮料理の店舗情報
INSERT IGNORE INTO stores (category_id, store_name, image_filename, rating, description, price, postal_code, address, phone_number, opening_hours, closed_days)
VALUES 
(11, 'シーフードハーバー', 'seafood01.jpg', 5, '新鮮な魚介をふんだんに使った料理が自慢です。', 5000, '460-0008', '名古屋市中区栄2-1-5', '052-123-4568', '11:00 - 22:00', '火曜日'),
(11, '海鮮レストランマリン', 'seafood02.jpg', 4, '海の幸を堪能できるおしゃれなレストラン。', 4500, '460-0008', '名古屋市中区栄2-1-6', '052-123-4569', '12:00 - 23:00', '水曜日'),
(11, 'シーフードカジュアル', 'seafood03.jpg', 3, 'リーズナブルな価格で楽しめる魚介料理店。', 3000, '460-0008', '名古屋市中区栄2-1-7', '052-123-4570', '10:00 - 21:00', '木曜日'),
(11, '豪華海鮮料理', 'seafood04.jpg', 5, '高級食材を使用した贅沢な海鮮料理。', 7000, '460-0008', '名古屋市中区栄2-1-8', '052-123-4571', '13:00 - 22:00', '金曜日'),
(11, '海鮮市場ダイナー', 'seafood05.jpg', 4, 'カジュアルな雰囲気で楽しめるシーフードレストラン。', 4000, '460-0008', '名古屋市中区栄2-1-9', '052-123-4572', '11:00 - 21:00', '月曜日');
-- ステーキの店舗情報
INSERT IGNORE INTO stores (category_id, store_name, image_filename, rating, description, price, postal_code, address, phone_number, opening_hours, closed_days)
VALUES 
(12, 'ステーキパレス', 'steak01.jpg', 5, 'ジューシーなステーキを提供する高級レストラン。', 5000, '460-0001', '名古屋市中区錦1-1-1', '052-123-4567', '17:00 - 23:00', '日曜日'),
(12, 'カジュアルステーキハウス', 'steak02.jpg', 4, '炭火焼きで香ばしいステーキが楽しめるカジュアルなステーキハウス。', 3000, '460-0002', '名古屋市中区錦1-1-2', '052-234-5678', '18:00 - 22:00', '月曜日'),
(12, 'ステキダイナー', 'steak03.jpg', 3, 'リーズナブルな価格で楽しめるステーキダイナー。', 2000, '460-0003', '名古屋市中区錦1-1-3', '052-345-6789', '11:00 - 21:00', '火曜日'),
(12, '高級ステーキレストラン', 'steak04.jpg', 5, '厳選された和牛ステーキを提供する隠れ家的なレストラン。', 8000, '460-0004', '名古屋市中区錦1-1-4', '052-456-7890', '17:00 - 23:00', '水曜日'),
(12, 'アメリカンステーキグリル', 'steak05.jpg', 4, 'ボリューム満点のステーキを楽しめるアメリカンスタイルのステーキハウス。', 4000, '460-0005', '名古屋市中区錦1-1-5', '052-567-8901', '12:00 - 22:00', '木曜日');
-- スイーツの店舗情報
INSERT IGNORE INTO stores (category_id, store_name, image_filename, rating, description, price, postal_code, address, phone_number, opening_hours, closed_days)
VALUES 
(13, 'パンケーキカフェ', 'sweets01.jpg', 5, 'ふわふわのパンケーキと贅沢なデザートが楽しめるカフェ。', 1500, '460-0008', '名古屋市中区栄3-1-1', '052-234-5678', '10:00 - 20:00', '火曜日'),
(13, 'フルーツタルトデリ', 'sweets02.jpg', 4, '新鮮なフルーツを使用したタルトが人気のお店。', 1800, '460-0008', '名古屋市中区栄3-1-2', '052-234-5679', '11:00 - 21:00', '水曜日'),
(13, 'ケーキベーカリーカフェ', 'sweets03.jpg', 3, '手作りケーキが自慢のベーカリーカフェ。', 1200, '460-0008', '名古屋市中区栄3-1-3', '052-234-5680', '09:00 - 19:00', '木曜日'),
(13, 'デラックスデザート', 'sweets04.jpg', 5, '高級感のあるデザートを提供するスイーツショップ。', 2000, '460-0008', '名古屋市中区栄3-1-4', '052-234-5681', '12:00 - 22:00', '金曜日'),
(13, '和洋スイーツカフェ', 'sweets05.jpg', 4, '和洋折衷のスイーツが楽しめるカジュアルなカフェ。', 1400, '460-0008', '名古屋市中区栄3-1-5', '052-234-5682', '10:00 - 20:00', '月曜日');
-- うどんの店舗情報
INSERT IGNORE INTO stores (category_id, store_name, image_filename, rating, description, price, postal_code, address, phone_number, opening_hours, closed_days)
VALUES 
(14, 'うどん古来屋', 'udon01.jpg', 5, '手打ちうどんが自慢の老舗うどん店。', 1000, '460-0001', '名古屋市中区錦1-1-1', '052-123-4561', '10:00 - 20:00', '月曜日'),
(14, '釜揚げうどん工房', 'udon02.jpg', 4, 'シンプルで美味しい釜揚げうどんが楽しめます。', 900, '460-0002', '名古屋市中区錦1-1-2', '052-123-4562', '11:00 - 21:00', '火曜日'),
(14, 'セルフうどんスタンド', 'udon03.jpg', 3, '手軽に食べられるセルフサービスのうどん店。', 700, '460-0003', '名古屋市中区錦1-1-3', '052-123-4563', '9:00 - 19:00', '水曜日'),
(14, '創作うどんアトリエ', 'udon04.jpg', 5, '創作うどんが人気のモダンなうどん店。', 1200, '460-0004', '名古屋市中区錦1-1-4', '052-123-4564', '10:00 - 22:00', '木曜日'),
(14, 'うどん家族', 'udon05.jpg', 4, '家庭的な雰囲気で楽しめるうどん店。', 800, '460-0005', '名古屋市中区錦1-1-5', '052-123-4565', '11:00 - 20:00', '金曜日');
-- 洋食の店舗情報
INSERT IGNORE INTO stores (category_id, store_name, image_filename, rating, description, price, postal_code, address, phone_number, opening_hours, closed_days)
VALUES 
(15, 'グランド洋食レストラン', 'westernfood01.jpg', 5, '厳選された食材を使用した本格洋食レストラン。', 4500, '460-0008', '名古屋市中区栄3-1-1', '052-123-4561', '11:30 - 22:00', '月曜日'),
(15, '洋食カフェテリア', 'westernfood02.jpg', 4, 'カジュアルな雰囲気で楽しめる洋食カフェ。', 3000, '460-0008', '名古屋市中区栄3-1-2', '052-123-4562', '10:00 - 21:00', '火曜日'),
(15, '洋食ダイナー', 'westernfood03.jpg', 3, 'リーズナブルな価格で楽しめる洋食ダイナー。', 2000, '460-0008', '名古屋市中区栄3-1-3', '052-123-4563', '11:00 - 20:00', '水曜日'),
(15, 'ラグジュアリーウェスタン', 'westernfood04.jpg', 5, '贅沢なディナーを提供する高級洋食レストラン。', 7000, '460-0008', '名古屋市中区栄3-1-4', '052-123-4564', '17:00 - 23:00', '木曜日'),
(15, '洋食バー＆グリル', 'westernfood05.jpg', 4, 'おしゃれな雰囲気で楽しめる洋食バー。', 3500, '460-0008', '名古屋市中区栄3-1-5', '052-123-4565', '12:00 - 23:00', '金曜日');


-- レビューテーブル --
-- カフェの画像レビュー
INSERT IGNORE INTO reviews (member_id, store_id, rating, comment)
VALUES
(1, 1, 4, 'おしゃれなカフェでコーヒーがとても美味しかったです。'),
(1, 2, 5, '居心地が良くて、友達と素敵な時間を過ごせました。'),
(1, 3, 3, 'レトロな雰囲気が好きな人にはたまらないカフェです。'),
(1, 4, 4, '落ち着いた雰囲気でのんびりできました。'),
(1, 5, 5, 'モダンなデザインで、とても美味しいコーヒーを楽しめました。');
-- 鍋料理のレビュー
INSERT IGNORE INTO reviews (member_id, store_id, rating, comment)
VALUES
(1, 6, 4, '美味しい豆腐鍋でした。'),
(1, 7, 5, '辛味が効いた麻辣火鍋で最高でした！'),
(1, 8, 4, '伝統的なすき焼き鍋が楽しめました。'),
(1, 9, 5, '具沢山のキムチ鍋が美味しかったです。'),
(1, 10, 3, '伝統的な日本の鍋料理が味わえました。');
-- 手羽先のレビュー
INSERT IGNORE INTO reviews (member_id, store_id, rating, comment)
VALUES
(1, 11, 4, '秘伝のタレが美味しくて、手羽先がとてもジューシーでした。'),
(1, 12, 5, 'サクサクの衣が最高で、ビールと一緒に楽しみました。'),
(1, 13, 3, 'リーズナブルな価格ですが、味付けがやや濃すぎました。'),
(1, 14, 4, '香ばしくてちょっと辛めの手羽先が美味しかったです。'),
(1, 15, 5, '甘辛のタレが絶品で、手羽先がとてもジューシーでした。'),
(2, 11, 4, '名古屋の名物を楽しむにはぴったりのお店です。'),
(2, 12, 5, '手羽先の衣がサクサクで、本当に美味しかったです。'),
(2, 13, 3, '味は良いですが、少し塩辛すぎました。'),
(2, 14, 4, '香ばしい手羽先が好きな人にはおすすめです。'),
(2, 15, 5, 'タレがとても美味しく、手羽先がジューシーでした。');
-- カレーのレビュー
INSERT IGNORE INTO reviews (member_id, store_id, rating, comment)
VALUES
(1, 16, 4, 'スパイスの効いた本格的なインドカレーで満足しました。'),
(1, 17, 5, 'ココナッツミルクの風味が絶妙で、クリーミーな味わいが素晴らしかったです。'),
(1, 18, 3, 'ピーナッツの風味が良かったですが、少し塩辛かったです。'),
(1, 19, 5, 'バターチキンカレーはとても美味しく、リピート確定です。'),
(1, 20, 2, 'ビーフカレーは辛すぎて、最後まで食べられませんでした。'),
(2, 16, 4, '名古屋で本格的なカレーを楽しめるお店です。'),
(2, 17, 5, 'ココナッツミルクのカレーが最高で、とてもクリーミーです。'),
(2, 18, 3, 'ピーナッツのカレーは風味が強く、好き嫌いが分かれるかもしれません。'),
(2, 19, 4, 'バターチキンカレーは美味しいですが、少し値段が高いと感じました。'),
(2, 20, 3, 'ビーフカレーは辛さが強く、辛い物が苦手な人には不向きかもしれません。');
-- カツのレビュー
INSERT IGNORE INTO reviews (member_id, store_id, rating, comment)
VALUES
(1, 21, 5, '特製ソースが絶品で、カツレツがとてもジューシーでした。'),
(1, 22, 4, 'サクサクの衣が良く、風味豊かなカツレツが楽しめました。'),
(1, 23, 3, 'コロッケカツはリーズナブルですが、やや油っぽかったです。'),
(1, 24, 4, '自家製ソースが美味しく、ボリューム満点でした。'),
(1, 25, 5, 'おしゃれな雰囲気で、カツレツも絶品でした。'),
(2, 21, 5, '名古屋の名物を堪能できる素晴らしいお店です。'),
(2, 22, 4, '衣がサクサクで、特製ソースも美味しかったです。'),
(2, 23, 3, '味は良いですが、少し油っぽく感じました。'),
(2, 24, 4, '香ばしいカツレツが好きな人にはおすすめです。'),
(2, 25, 5, 'タレがとても美味しく、カツレツがジューシーでした。');
-- 鰻店舗のレビュー
INSERT IGNORE INTO reviews (member_id, store_id, rating, comment)
VALUES
(1, 26, 5, '炭火焼きの香ばしさが最高でした。うなぎもふっくらしていてとても美味しかったです。'),
(1, 27, 4, '秘伝のタレがとても美味しかったです。ただ、少し待ち時間が長かったのが残念。'),
(1, 28, 3, '家庭的な雰囲気は良いですが、うなぎの味が少し淡白でした。'),
(2, 26, 5, '名古屋で一番美味しいうなぎだと思います。絶対にまた来ます。'),
(2, 27, 4, 'うなぎは美味しかったですが、接客が少し不親切でした。'),
(2, 28, 3, '価格はリーズナブルですが、味は普通でした。特別な感じはありませんでした。');
-- 和食のレビュー
INSERT IGNORE INTO reviews (member_id, store_id, rating, comment)
VALUES
(1, 29, 4, '味噌汁がとても美味しかったです。具材もたっぷりで満足しました。'),
(1, 30, 5, '寿司のネタが新鮮でとても美味しかったです。また行きたいです。'),
(1, 31, 4, '海鮮丼のボリュームがあり、満足しました。少し値段が高いですが、また行きたいです。'),
(1, 32, 5, '焼き魚がとても香ばしくて美味しかったです。定食のボリュームも満点でした。'),
(1, 33, 3, 'そばの風味が良かったですが、つゆが少し濃かったです。'),
(2, 29, 4, '地元の食材を使った味噌汁が美味しく、雰囲気も良かったです。'),
(2, 30, 5, '新鮮な魚介を使った寿司が絶品でした。接客も丁寧で良かったです。'),
(2, 31, 3, '海鮮丼の味は良かったですが、少し価格が高いと感じました。'),
(2, 32, 4, '焼き魚定食が美味しく、また行きたいと思いました。'),
(2, 33, 3, 'そばの味は良かったですが、量が少なく感じました。');
-- パスタのレビュー --
INSERT IGNORE INTO reviews (member_id, store_id, rating, comment)
VALUES
(1, 34, 4, '海老がたっぷりで満足しました。ソースも美味しかったです。'),
(1, 35, 5, 'ジェノベーゼパスタがとても美味しく、ヘルシーな一品でした。'),
(1, 36, 3, 'トマトとバジルのシンプルなスパゲッティですが、少し味が薄かったです。'),
(1, 37, 2, 'カルボナーラは少し重すぎました。もう少し軽いクリームソースが良かったです。'),
(1, 38, 5, 'ボロネーゼが絶品で、リピートしたいお店です。'),
(2, 34, 4, '海老が新鮮で美味しかったですが、少し値段が高いと感じました。'),
(2, 35, 5, 'バジルの香りが素晴らしく、ジェノベーゼパスタを堪能しました。'),
(2, 36, 3, '味は良いですが、トマトソースが少し濃すぎました。'),
(2, 37, 2, 'カルボナーラは重たくて、最後まで食べきれませんでした。'),
(2, 38, 5, 'ボロネーゼのソースが最高で、肉の旨味がしっかり感じられました。');
-- ピザのレビュー
INSERT IGNORE INTO reviews (member_id, store_id, rating, comment)
VALUES
(1, 39, 5, '本格的なナポリピッツァがとても美味しかったです。'),
(1, 40, 4, 'トッピングの選択肢が多く、自分好みのピザが作れました。'),
(1, 41, 5, '薪窯で焼いたピザが絶品で、雰囲気も良かったです。'),
(1, 42, 3, 'テイクアウト専門ですが、味はまずまず。'),
(1, 43, 4, 'チーズたっぷりで満足しましたが、少し塩辛かったです。'),
(2, 39, 5, 'ナポリピッツァが最高で、サービスも良かったです。'),
(2, 40, 4, 'カスタムピザが楽しく、自分好みの味にできました。'),
(2, 41, 5, '薪窯で焼いたピザは香ばしくて美味しかったです。'),
(2, 42, 3, 'テイクアウトには便利ですが、味は普通でした。'),
(2, 43, 4, 'チーズが多くて美味しいですが、少し重かったです。');
-- ラーメンのレビュー
INSERT IGNORE INTO reviews (member_id, store_id, rating, comment)
VALUES
(1, 44, 5, '濃厚なスープが最高で、チャーシューも柔らかくて美味しかったです。'),
(1, 45, 4, '辛味噌ラーメンがとても美味しかったですが、少し辛すぎました。'),
(1, 46, 3, '家庭的な雰囲気は良いですが、ラーメンの味が少し薄かったです。'),
(1, 47, 5, '魚介系の出汁が効いていて、塩ラーメンが絶品でした。'),
(1, 48, 4, '担々麺がピリ辛で美味しかったですが、少し高いと感じました。'),
(2, 44, 5, '名古屋で一番美味しいラーメンだと思います。絶対にまた来ます。'),
(2, 45, 4, 'ラーメンは美味しかったですが、サービスが少し不親切でした。'),
(2, 46, 3, '価格はリーズナブルですが、味は普通でした。特別な感じはありませんでした。'),
(2, 47, 4, '塩ラーメンが好きな人にはたまらないお店です。'),
(2, 48, 3, '担々麺は美味しいですが、辛さが強くて最後まで食べられませんでした。');
-- 魚介・海鮮料理のレビュー
INSERT IGNORE INTO reviews (member_id, store_id, rating, comment)
VALUES
(1, 49, 5, '新鮮な魚介を使った料理がとても美味しかったです。'),
(1, 50, 4, 'おしゃれな雰囲気で、料理も美味しかったです。ただ、少し高めの価格設定です。'),
(1, 51, 3, 'リーズナブルで良かったですが、味は普通でした。'),
(1, 52, 5, '高級感溢れる雰囲気で、料理も最高でした。また行きたいです。'),
(1, 53, 4, 'カジュアルな雰囲気で、料理も美味しかったです。ただ、サービスが少し遅かったです。'),
(2, 49, 5, '絶品の魚介料理を楽しめました。再訪します。'),
(2, 50, 4, '素敵なレストランでしたが、少し高価でした。'),
(2, 51, 3, '価格は手頃ですが、味は特に印象に残りませんでした。'),
(2, 52, 5, '豪華な海鮮料理を堪能しました。とても満足です。'),
(2, 53, 4, 'カジュアルに楽しめる良いお店ですが、サービスの改善が必要です。');
-- ステーキのレビュー
INSERT IGNORE INTO reviews (member_id, store_id, rating, comment)
VALUES
(1, 54, 5, 'ステーキがとてもジューシーで最高でした。サービスも良かったです。'),
(1, 55, 4, '炭火焼きの香ばしいステーキが美味しかったです。ただし、少し焼きすぎでした。'),
(1, 56, 3, 'リーズナブルですが、味は普通でした。特に印象に残るものはなかったです。'),
(1, 57, 5, '和牛ステーキが絶品で、また来たいと思いました。価格が高いですがその価値があります。'),
(1, 58, 4, 'ボリューム満点で満足しましたが、少し塩味が強かったです。'),
(2, 54, 5, '高級感があり、特別な日に訪れたいお店です。ステーキも文句なしに美味しかったです。'),
(2, 55, 4, 'カジュアルな雰囲気で楽しめました。ステーキも美味しかったですが、焼き加減が不均一でした。'),
(2, 56, 2, '値段相応の味とサービスでした。特におすすめする点はありません。'),
(2, 57, 5, '和牛ステーキが絶品で、サービスも一流でした。また来たいと思います。'),
(2, 58, 3, 'アメリカンスタイルのステーキが楽しめますが、少し油っぽかったです。');
-- スイーツのレビュー
INSERT IGNORE INTO reviews (member_id, store_id, rating, comment)
VALUES
(1, 59, 5, 'パンケーキがふわふわで、とても美味しかったです。'),
(1, 60, 4, 'タルトが新鮮で美味しかったですが、少し高めです。'),
(1, 61, 3, '手作りケーキが美味しかったですが、特別感はありませんでした。'),
(1, 62, 5, '高級感のあるデザートが楽しめて大満足です。'),
(1, 63, 4, '和洋折衷のスイーツが美味しかったですが、サービスが少し遅かったです。'),
(2, 59, 5, 'ふわふわのパンケーキに感動しました。また来たいです。'),
(2, 60, 4, 'フルーツタルトがとても美味しかったですが、少し高価でした。'),
(2, 61, 3, 'ケーキは普通でしたが、価格はリーズナブルでした。'),
(2, 62, 5, '豪華なデザートが堪能できてとても満足です。'),
(2, 63, 4, 'カジュアルな雰囲気でスイーツが楽しめますが、サービスの改善が必要です。');
-- うどんのレビュー
INSERT IGNORE INTO reviews (member_id, store_id, rating, comment)
VALUES
(1, 64, 5, '手打ちうどんがとても美味しかったです。また行きたいです。'),
(1, 65, 4, 'シンプルで美味しい釜揚げうどんが楽しめました。少し値段が高めです。'),
(1, 66, 3, 'セルフサービスのため、少し不便ですが、味は良かったです。'),
(1, 67, 5, '創作うどんが美味しく、雰囲気も良かったです。'),
(1, 68, 4, '家庭的な雰囲気で楽しめました。ただ、店員の対応が少し冷たかったです。'),
(2, 64, 5, '絶品の手打ちうどんを楽しめました。再訪します。'),
(2, 65, 4, '素敵なうどん店でしたが、少し高価でした。'),
(2, 66, 3, '価格は手頃ですが、セルフサービスが不便でした。'),
(2, 67, 5, '豪華な創作うどんを堪能しました。とても満足です。'),
(2, 68, 4, '家庭的なうどん店で、料理も美味しかったです。ただ、サービスの改善が必要です。');
-- 洋食のレビュー
INSERT IGNORE INTO reviews (member_id, store_id, rating, comment)
VALUES
(1, 69, 5, '料理がとても美味しく、サービスも最高でした。また来たいです。'),
(1, 70, 4, 'カジュアルな雰囲気が良く、料理も美味しかったです。ただ、もう少し種類が欲しかったです。'),
(1, 71, 3, '価格に見合った味でした。特に印象に残ることはありませんでした。'),
(1, 72, 5, '贅沢なディナーを堪能しました。雰囲気も素晴らしかったです。'),
(1, 73, 4, 'おしゃれな雰囲気が気に入りました。料理も美味しかったですが、少し高めの価格設定です。'),
(2, 69, 5, '絶品の洋食料理を楽しめました。再訪したいです。'),
(2, 70, 4, '素敵なカフェでしたが、少し混雑していました。'),
(2, 71, 3, '価格は手頃ですが、味は特に印象に残りませんでした。'),
(2, 72, 5, '豪華なディナーを堪能しました。とても満足です。'),
(2, 73, 4, 'カジュアルに楽しめる良いお店ですが、サービスの改善が必要です。');