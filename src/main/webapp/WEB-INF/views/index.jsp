<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Mosaic | Dashboard</title>
<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<!-- Bootstrap 3.3.5 -->
<link rel="stylesheet"
	href="<c:url value="/resources/bootstrap/css/bootstrap.min.css"/>">
<!-- Font Awesome -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
<!-- Ionicons -->
<link rel="stylesheet"
	href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
<!-- Theme style -->
<link rel="stylesheet"
	href="<c:url value="/resources/css/AdminLTE.min.css"/>">
<!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet"
	href="<c:url value="/resources/css/skins/_all-skins.min.css"/>">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->

<style>
html, body, .wrapper, .content-wrapper, .content, #map {
	height: 100%;
}

#map {
	margin: 0px;
}

.content-wrapper {
	position: relative;
}

#color-palette {
	position: absolute;
	left: 10px;
	top: 90px;
	z-index: 999;
}

.color-button {
	width: 14px;
	height: 14px;
	font-size: 0;
	margin: 2px;
	float: left;
	cursor: pointer;
}

#search-box {
	position: absolute;
	right: 5px;
	top: 55px;
	z-index: 999;
	width: 300px;
}

td {
	padding: 0 4px;
}
</style>
</head>

<body class="hold-transition fixed skin-black sidebar-mini">
	<div class="wrapper">

		<header class="main-header">

			<!-- Logo -->
			<a href="index.html" class="logo"> <!-- mini logo for sidebar mini 50x50 pixels -->
				<span class="logo-mini"><b>M</b>SC</span> <!-- logo for regular state and mobile devices -->
				<span class="logo-lg"><b>Mosaic</b> Demo</span>
			</a>

			<!-- Header Navbar: style can be found in header.less -->
			<nav class="navbar navbar-static-top" role="navigation">
				<!-- Sidebar toggle button-->
				<a href="#" class="sidebar-toggle" data-toggle="offcanvas"
					role="button"> <span class="sr-only">Toggle navigation</span>
				</a>
				<!-- Navbar Right Menu -->
				<div class="navbar-custom-menu">
					<ul class="nav navbar-nav">
						<!-- User Account: style can be found in dropdown.less -->
						<li class="dropdown user user-menu"><a href="#"
							class="dropdown-toggle" data-toggle="dropdown"> <span
								class="hidden-xs">Mosaic Admin</span>
						</a>
							<ul class="dropdown-menu">
								<!-- User image -->
								<li class="user-header"><img src=""
									<c:url value="/resources/img/avatar04.png" />"
									class="img-circle"
									alt="User Image">
									<p>Mosaic Admin</p></li>
								<!-- Menu Body -->
								<li class="user-body">
									<div class="row">
										<div class="col-xs-4 text-center">
											<a href="#">Followers</a>
										</div>
										<div class="col-xs-4 text-center">
											<a href="#">Sales</a>
										</div>
										<div class="col-xs-4 text-center">
											<a href="#">Friends</a>
										</div>
									</div> <!-- /.row -->
								</li>
								<!-- Menu Footer-->
								<li class="user-footer">
									<div class="pull-left">
										<a href="#" class="btn btn-default btn-flat">Profile</a>
									</div>
									<div class="pull-right">
										<a href="#" class="btn btn-default btn-flat">Sign out</a>
									</div>
								</li>
							</ul></li>
						<!-- Control Sidebar Toggle Button -->
						<li><a href="#" data-toggle="control-sidebar"><i
								class="fa fa-gears"></i></a></li>
					</ul>
				</div>

			</nav>
		</header>
		<!-- Left side column. contains the logo and sidebar -->
		<aside class="main-sidebar">
			<!-- sidebar: style can be found in sidebar.less -->
			<section class="sidebar">
				<!-- sidebar menu: : style can be found in sidebar.less -->
				<ul class="sidebar-menu">
					<li class="header">MAIN NAVIGATION</li>
					<li class="active treeview"><a href="#"> <i
							class="fa fa-dashboard"></i> <span>Dashboard</span> <i
							class="fa fa-angle-left pull-right"></i>
					</a>
						<ul class="treeview-menu">
							<li class="active"><a href="index.html"><i
									class="fa fa-globe"></i> Map</a></li>
						</ul></li>
				</ul>
			</section>
			<!-- /.sidebar -->
		</aside>

		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper">
			<!-- Content Header (Page header) -->
			<section class="content-header" style="display: none;">
				<h1>
					Dashboard <small>Version 1.0</small>
				</h1>
				<ol class="breadcrumb">
					<li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
					<li class="active">Dashboard</li>
				</ol>
			</section>

			<div class="box box-primary" id="search-box">
				<div class="box-header with-border">
					<h3 class="box-title">Options</h3>
					<div class="box-tools pull-right">
						<button type="button" class="btn btn-box-tool"
							data-widget="collapse">
							<i class="fa fa-minus"></i>
						</button>
					</div>
				</div>
				<!-- <form role="form"> -->
				<div class="box-body">
					<div class="form-group">
						<label for="nnearest">N-Nearest</label> <input type="text"
							class="form-control" id="nnearest" placeholder="N-Nearest"
							value="10">
					</div>
					<div class="form-group">
						<label for="dwithin">DWithin</label> <input type="text"
							class="form-control" id="dwithin" placeholder="DWithin"
							value="100">
					</div>
					<div class="form-group">
						<label for="geojson">Geojson</label>
						<textarea class="form-control" rows="10" id="geojson">
{"type":"Polygon","coordinates":[[[126.92837422258921,37.49316229879263],[126.92935757832086,37.493361366300206],[126.93131459593648,37.49361426890266],[126.93226894307669,37.493742073302606],[126.9324019029896,37.49375887233195],[126.9331268345503,37.493844986395246],[126.93371675092567,37.4938954503005],[126.93461666764615,37.49394268312084],[126.93491798569086,37.493940731827884],[126.93753776610684,37.49425237569231],[126.93760079731975,37.4937225146682],[126.93823049501061,37.49359094681767],[126.93854435750724,37.49351318368573],[126.93943167628467,37.493244133499715],[126.94014828496606,37.49298584298709],[126.94072907750031,37.49275339434591],[126.94100892189633,37.49262994321975],[126.94165540283066,37.492345403329246],[126.94178488176756,37.49228719163296],[126.94289189731249,37.491778961602364],[126.9439412221798,37.491312188011285],[126.9444295992836,37.491259213927165],[126.94572857131983,37.49112753591843],[126.94688390018702,37.4910277809232],[126.9469931101964,37.49101768060681],[126.94819751251984,37.49089888549796],[126.94852974451487,37.49086868936634],[126.9486841049614,37.49085331737873],[126.95004131557596,37.490706336517185],[126.95266216452954,37.49046911144851],[126.95306251768204,37.490423818215916],[126.95385967037507,37.49031551172418],[126.95429113117451,37.49024618394994],[126.95448555893351,37.490210080511055],[126.95502503796014,37.490092680285635],[126.955080588849,37.49007879400369],[126.95622626206196,37.48970942852634],[126.95677900310895,37.48948942327189],[126.95888494579759,37.488662523124695],[126.95901729496003,37.48861516467026],[126.95962557932727,37.488387424231966],[126.95980879298321,37.488316540080646],[126.96088558324513,37.48788636800851],[126.96092822937258,37.487870481564315],[126.961028357705,37.4878325020834],[126.96227270239632,37.48735202460838],[126.96230749879139,37.48733850572342],[126.96326009406374,37.48696613353828],[126.96334128347037,37.48693569602357],[126.96491060241412,37.48631912906227],[126.96498793330278,37.486288333108824],[126.96574695980152,37.4859819871742],[126.96692482997642,37.485508169707174],[126.96909902862528,37.484648992067505],[126.9691076697478,37.4846455721978],[126.9694159784576,37.484523369688574],[126.9694533207033,37.484508471966464],[126.969664262826,37.484423769786446],[126.9696833350294,37.484428724740084],[126.96987209611659,37.484475593534775],[126.97061091282059,37.484650586432444],[126.97076267828001,37.484685149731405],[126.97249028815023,37.485062914554234],[126.972874964426,37.485138290904224],[126.9733554738932,37.48522162315986],[126.97370505545715,37.485275196804366],[126.97493549438973,37.485439088684075],[126.9754734278415,37.48549435090182],[126.97685385492223,37.48559436399963],[126.97703102900013,37.4856054450741],[126.97718934768736,37.48561378027388],[126.9773951631748,37.48562225550697],[126.97823675252991,37.48564726767035],[126.97868998208735,37.48564932039113],[126.98124252900622,37.48559659444511],[126.98189840982674,37.485559064197645],[126.98355381908556,37.48540355635084],[126.98420442961982,37.48531841413149],[126.98605427001344,37.485007372279625],[126.98650939106332,37.484918734180894],[126.98757204133054,37.48468321092165],[126.98771524584983,37.48465283771515],[126.9882170474177,37.48453126757341],[126.9897946937377,37.48410079027796],[126.98979729718381,37.484100079481045],[126.99084443216441,37.4838140203535],[126.99087610690596,37.483805305208115],[126.99205951710088,37.483477371335745],[126.99340677883656,37.48311047036848],[126.99351419722727,37.483080500923286],[126.99351632796744,37.48307989219875],[126.99477110134056,37.48274502942662],[126.9956257509261,37.482573308733215],[126.99592409203547,37.48255632041861],[126.99632986016742,37.482567710345705],[126.99659315987562,37.48264897907037],[126.99804437871825,37.48329791594484],[127.00026663212671,37.48435754341142],[127.00295798332748,37.485652522448746],[127.00424819583543,37.48631059870258],[127.00432995921996,37.48635177964346],[127.00585084069483,37.48710811389342],[127.00653148182391,37.48744977750366],[127.00817152785511,37.48827946824615],[127.00834255894945,37.48836372873814],[127.00908970305835,37.488722033852596],[127.00980923489888,37.489029377988366],[127.01002310204962,37.48910992802335],[127.01045588947059,37.48926046653113],[127.0109252869607,37.489410457527654],[127.0110470236419,37.48944840885738],[127.01334957210328,37.49014836885288],[127.01354178315344,37.4902044717698],[127.01411224858563,37.490364115666154],[127.01502508116148,37.49064346417374],[127.01512536806935,37.49067351755471],[127.01554199336832,37.49079573305486],[127.01554382152345,37.490796269127316],[127.01618820198996,37.490985147636906],[127.0170727732201,37.49119654357429],[127.01871428186817,37.49150209308766],[127.01905916923067,37.49155938306975],[127.02048403367462,37.49176771776795],[127.02050282134559,37.49177019608046],[127.0207816868806,37.491816369928884],[127.0221801476518,37.49263869217815],[127.02246067398922,37.49276923760481],[127.02406027815698,37.49333707911628],[127.02573805116283,37.49359448000303],[127.02743431446612,37.49353228449177],[127.0276870701837,37.49349895956503],[127.02910762953843,37.493193349571825],[127.03046049431293,37.49266312014023],[127.03118266078266,37.492310396291614],[127.03118266078266,37.49231039629162],[127.03263946704476,37.491418451220035],[127.03390150979025,37.49026732194642],[127.03492332664813,37.488898475422886],[127.035009433926,37.48875682675408],[127.035796720774,37.48710284783638],[127.03623280983672,37.485323720755304],[127.03629963589944,37.48349314676921],[127.03628853058666,37.48332372180423],[127.03610926326502,37.482232992711815],[127.03689281889585,37.48026808761049],[127.03714434562363,37.47971629922607],[127.03727451690155,37.47952171078761],[127.03728557673759,37.47950876808507],[127.0384344778612,37.47829653289323],[127.03852087315647,37.478204081882495],[127.0389432089728,37.47774572616318],[127.04258418572283,37.47387952936134],[127.04424861440978,37.47211557743837],[127.04597076880366,37.47034766153901],[127.04606575954857,37.47024864770814],[127.04696848219187,37.469293219296354],[127.0471079610733,37.46914217769116],[127.04741627600866,37.46880055622596],[127.04742557788822,37.46879023347596],[127.04870521613573,37.46736795011679],[127.05306321849328,37.46288050250003],[127.05319106865007,37.46274611113768],[127.05353271459617,37.462379493169784],[127.05354965126655,37.4623612681004],[127.0550812822259,37.46070854835291],[127.05570180004773,37.460044533323675],[127.05580080870234,37.45993684307462],[127.057614589261,37.45793155289066],[127.05762548477817,37.45791948506785],[127.05914483865406,37.45623359695625],[127.05920756925828,37.45616325335377],[127.06204073560833,37.45295256583866],[127.0620693377724,37.45291999465057],[127.0633581511071,37.45144518899535],[127.06384637945028,37.45083493712054],[127.06611696187842,37.44772949946451],[127.0674089800057,37.44601770572812],[127.0675613964821,37.44580963450615],[127.06817478271269,37.444946596773136],[127.06838147652499,37.44467592184956],[127.06907006435006,37.44392556998456],[127.07000080847949,37.443006763016925],[127.07003157638474,37.44297624310568],[127.07435489501106,37.43866707078516],[127.0780504012864,37.43501884795469],[127.07806251354408,37.43500686801462],[127.08146507721467,37.4316350982065],[127.08147846571664,37.431621803028285],[127.08493935947682,37.428177820409786],[127.08494683900699,37.428170368690566],[127.09055175995147,37.42257973375875],[127.09678978034547,37.416358718131576],[127.09828633261309,37.414902415342],[127.09869188175652,37.41458094803934],[127.09919588614166,37.41415028134489],[127.10218520083126,37.41139912972847],[127.10352542968083,37.41024974419211],[127.10475942160909,37.40922985674225],[127.1054740732258,37.408571114703406],[127.10639623840015,37.40762401892356],[127.10722672110745,37.406599094164335],[127.11935946432074,37.40652231990163],[127.11929421918678,37.39621154089411],[127.1191150679061,37.394480808568915],[127.1186062273074,37.39281689426584],[127.11778671627901,37.39128198995703],[127.11668716560942,37.38993346561989],[127.11534867310303,37.38882172492476],[127.11382126747137,37.38798832130153],[127.11216203841448,37.387464404800895],[127.11087255509635,37.38731909145364],[127.11021138948912,37.38685399444306],[127.10872735729087,37.38617109522355],[127.10859403740182,37.38612387608289],[127.10711378253987,37.38573753773397],[127.10558947984525,37.38560748746913],[127.1054478260471,37.38560748492392],[127.10397058539299,37.38572952131625],[127.10253341064804,37.38609237332973],[127.10192023515368,37.38636047751404],[127.10163879449111,37.38643624246198],[127.09945749745228,37.38734468400265],[127.09750208747681,37.38848062820809],[127.09613265819723,37.3894580302828],[127.09594655944605,37.3896191193635],[127.09490312665119,37.39068595320599],[127.09405031817697,37.3919105364621],[127.0934115797966,37.393259202368206],[127.09232351819368,37.39615592799562],[127.09199506096276,37.396427396066],[127.0918697359474,37.39653291095386],[127.09034761517512,37.39783828771905],[127.0901118661849,37.3980477455048],[127.08725057815357,37.400681070534034],[127.08674667992766,37.40108049636472],[127.08674667992766,37.40108049636471],[127.0860607298205,37.40168335090184],[127.08419695909294,37.40349699588334],[127.08411832259516,37.403574462002595],[127.07784093855707,37.409834733969035],[127.07784037262276,37.409835298410755],[127.07223890625471,37.41542248757128],[127.06878844172344,37.418856091921754],[127.06539862973837,37.42221522544958],[127.06169381434304,37.425872638291246],[127.0616631455226,37.42590306038698],[127.05733987620286,37.430212183562254],[127.05626647665473,37.431271816336384],[127.05595821492273,37.431591521830946],[127.0548443930369,37.43280524983902],[127.05432245687041,37.43342827988793],[127.05377526416893,37.434144853553136],[127.05359229743911,37.43439316538195],[127.05296427299216,37.435276799164306],[127.05170871013735,37.4369402933407],[127.0516270584957,37.43705020234459],[127.0495469220469,37.43989517021614],[127.04852974515072,37.44105913880529],[127.0457421002337,37.44421823912831],[127.04425973814098,37.445863080839544],[127.04250047749775,37.44780809457457],[127.04191712456917,37.448432339516934],[127.04189162743393,37.448459737934584],[127.04035573213103,37.450117059159176],[127.04008579970343,37.45040672183249],[127.03567291655563,37.45495068035753],[127.03543869506788,37.45520123982287],[127.03404898064568,37.456745869877906],[127.03381419743934,37.45700601615613],[127.03302919485077,37.45783685138775],[127.03130495522547,37.459606907927835],[127.03120586146323,37.459710267221425],[127.02948929957732,37.461529469607974],[127.02948330779823,37.46153582583766],[127.02580575430417,37.465440861912704],[127.02573897964578,37.4655125447588],[127.02532632425614,37.46596039443349],[127.0240615935754,37.4672948443769],[127.02375174991545,37.46763909266527],[127.02324622364594,37.468230681556285],[127.02260784669231,37.46907330718985],[127.021777331635,37.470314814768926],[127.02106856510426,37.47158596653806],[127.02042413936115,37.47299967967624],[127.02025362602305,37.47339899596336],[127.02024634679874,37.47341724991294],[127.02024232975549,37.47341607152807],[127.01927582044272,37.47312029666065],[127.01906761984333,37.47305931679658],[127.01848910418948,37.472897420057755],[127.01638504535514,37.472257799638506],[127.01621195820987,37.47217479288163],[127.01464442370501,37.47138178534254],[127.01461928841563,37.4713691188545],[127.01391102957675,37.47101359189389],[127.0138809211252,37.47099854867622],[127.01238595777806,37.47025510350935],[127.01104354720722,37.46957040349744],[127.01085652241474,37.46947772806044],[127.00805681523086,37.46813061211632],[127.00802817859494,37.468116895380206],[127.00569231149684,37.467003093990144],[127.00549261763507,37.466910852896234],[127.00344283004436,37.46599425595485],[127.00242324893973,37.465610556462046],[127.00062342841147,37.46505503317659],[126.99902959013355,37.46471741324139],[126.9985379698546,37.46465907787423],[126.99773000595079,37.46459992254454],[126.99604682713677,37.46455267565039],[126.99528264181234,37.46456368774697],[126.99396609285202,37.464638655450806],[126.99270485258207,37.46480044722622],[126.99094944684296,37.46515315258737],[126.99040171794061,37.46528113345433],[126.98879907396409,37.4657088328431],[126.98864742661169,37.465750726377586],[126.98862327164007,37.46575762713632],[126.98731052089238,37.46611512969603],[126.98727196232701,37.466125722450656],[126.98608511403275,37.46645460905204],[126.98505512604982,37.466735983917026],[126.98372203785515,37.4670997309707],[126.98371711806668,37.46710079788804],[126.98284085227151,37.46729501133945],[126.98154346680211,37.46751316052701],[126.98054217077157,37.46760722149042],[126.9785448902286,37.46764847773739],[126.97804822362556,37.46763371673056],[126.97704393080569,37.46756095487488],[126.97625694530649,37.467456130060455],[126.97614385929369,37.467436518141916],[126.97468379879432,37.4671172565393],[126.9741153491458,37.466982616038294],[126.97342899766655,37.46680430209706],[126.97257193828027,37.466625633714834],[126.9714303806165,37.46644507870022],[126.9714303806165,37.466445078700225],[126.97011254349016,37.46633500624442],[126.96897653610336,37.466323876093945],[126.96764970442307,37.46640908901109],[126.96673034006973,37.466536835495425],[126.96490002560573,37.46699061080074],[126.96373623076524,37.467412763156446],[126.96345158539503,37.46752150182615],[126.96276474056725,37.46779729906143],[126.96247943929484,37.46791038224807],[126.96028393651012,37.46877797863879],[126.96023274783394,37.46879838826594],[126.95902451080225,37.469284421295825],[126.95901490550293,37.46928829160578],[126.95828967170529,37.46958099867981],[126.95680718951635,37.470163448501154],[126.95673462552195,37.47019058119281],[126.95577153261306,37.47056705693375],[126.9545945513234,37.47102152345107],[126.95450307321295,37.47105560068114],[126.95430594809234,37.4711316801751],[126.95322226183681,37.47156460716057],[126.95279366695523,37.47172507221843],[126.95245837939109,37.47184806583182],[126.95058536496578,37.47258350650281],[126.94833977429195,37.47278676522212],[126.94818208804902,37.47280243886491],[126.94682321729002,37.47294959951392],[126.94653382953145,37.47297590158892],[126.94646505009254,37.472982419126836],[126.94528084479067,37.47309922212114],[126.9441133666513,37.47320002612556],[126.94397988985804,37.47321255317661],[126.94258279086063,37.47335417840706],[126.94251994357748,37.47336077227959],[126.9413422694371,37.47348851415876],[126.94045087174102,37.47363073688045],[126.93953150553777,37.47382514230037],[126.93806724067957,37.47426763716959],[126.93732007812585,37.47456481150644],[126.9369883652851,37.47470449128474],[126.93552736891466,37.47535438965619],[126.93543020935296,37.475398300598236],[126.93463813529875,37.47576194121976],[126.93376370044142,37.475215061058194],[126.93208579007626,37.474584632466524],[126.9303162246904,37.474299217421695],[126.92852519318609,37.47437013676489],[126.92678373590817,37.47479457751679],[126.92516092686526,37.47555570445292],[126.92378880654172,37.476375029180176],[126.9224047962834,37.4773923669541],[126.92123927002088,37.47865411537306],[126.92033468255967,37.480114314697275],[126.91972398389498,37.481719776525644],[126.91942941899364,37.48341202120662],[126.91946171751142,37.48512940798517],[126.91981970296064,37.48680938029433],[126.92049033556424,37.48839074440559],[126.92144918723535,37.489815898437335],[126.92266133138116,37.491032930528604],[126.92408261511926,37.491997509751485],[126.92566126756587,37.492674500884526],[126.92733978561293,37.493039244228655],[126.92837422258921,37.49316229879263]]]}

            </textarea>
					</div>
					<div class="box-footer">
						<button class="btn btn-default" id="draw-geojson">Draw</button>
						<button class="btn btn-danger pull-right" id="delete-all-button">Delete</button>
					</div>
				</div>
				<!-- </form> -->
			</div>

			<div id="color-palette"></div>

			<!-- Main content -->
			<section class="content" id="map"></section>
			<!-- /.content -->
		</div>
		<!-- /.content-wrapper -->

		<!-- Control Sidebar -->
		<aside class="control-sidebar control-sidebar-dark">
			<!-- Create the tabs -->
			<ul class="nav nav-tabs nav-justified control-sidebar-tabs">
				<li><a href="#control-sidebar-home-tab" data-toggle="tab"><i
						class="fa fa-home"></i></a></li>
				<li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i
						class="fa fa-gears"></i></a></li>
			</ul>
			<!-- Tab panes -->
			<div class="tab-content">
				<!-- Home tab content -->
				<div class="tab-pane" id="control-sidebar-home-tab">
					<h3 class="control-sidebar-heading">Recent Activity</h3>
					<ul class="control-sidebar-menu">
						<li><a href="javascript::;"> <i
								class="menu-icon fa fa-birthday-cake bg-red"></i>

								<div class="menu-info">
									<h4 class="control-sidebar-subheading">Langdon's Birthday</h4>

									<p>Will be 23 on April 24th</p>
								</div>
						</a></li>
						<li><a href="javascript::;"> <i
								class="menu-icon fa fa-user bg-yellow"></i>

								<div class="menu-info">
									<h4 class="control-sidebar-subheading">Frodo Updated His
										Profile</h4>

									<p>New phone +1(800)555-1234</p>
								</div>
						</a></li>
						<li><a href="javascript::;"> <i
								class="menu-icon fa fa-envelope-o bg-light-blue"></i>

								<div class="menu-info">
									<h4 class="control-sidebar-subheading">Nora Joined Mailing
										List</h4>

									<p>nora@example.com</p>
								</div>
						</a></li>
						<li><a href="javascript::;"> <i
								class="menu-icon fa fa-file-code-o bg-green"></i>

								<div class="menu-info">
									<h4 class="control-sidebar-subheading">Cron Job 254
										Executed</h4>

									<p>Execution time 5 seconds</p>
								</div>
						</a></li>
					</ul>
					<!-- /.control-sidebar-menu -->

					<h3 class="control-sidebar-heading">Tasks Progress</h3>
					<ul class="control-sidebar-menu">
						<li><a href="javascript::;">
								<h4 class="control-sidebar-subheading">
									Custom Template Design <span
										class="label label-danger pull-right">70%</span>
								</h4>

								<div class="progress progress-xxs">
									<div class="progress-bar progress-bar-danger"
										style="width: 70%"></div>
								</div>
						</a></li>
						<li><a href="javascript::;">
								<h4 class="control-sidebar-subheading">
									Update Resume <span class="label label-success pull-right">95%</span>
								</h4>

								<div class="progress progress-xxs">
									<div class="progress-bar progress-bar-success"
										style="width: 95%"></div>
								</div>
						</a></li>
						<li><a href="javascript::;">
								<h4 class="control-sidebar-subheading">
									Laravel Integration <span
										class="label label-warning pull-right">50%</span>
								</h4>

								<div class="progress progress-xxs">
									<div class="progress-bar progress-bar-warning"
										style="width: 50%"></div>
								</div>
						</a></li>
						<li><a href="javascript::;">
								<h4 class="control-sidebar-subheading">
									Back End Framework <span class="label label-primary pull-right">68%</span>
								</h4>

								<div class="progress progress-xxs">
									<div class="progress-bar progress-bar-primary"
										style="width: 68%"></div>
								</div>
						</a></li>
					</ul>
					<!-- /.control-sidebar-menu -->

				</div>
				<!-- /.tab-pane -->

				<!-- Settings tab content -->
				<div class="tab-pane" id="control-sidebar-settings-tab">
					<form method="post">
						<h3 class="control-sidebar-heading">General Settings</h3>

						<div class="form-group">
							<label class="control-sidebar-subheading"> Report panel
								usage <input type="checkbox" class="pull-right" checked>
							</label>

							<p>Some information about this general settings option</p>
						</div>
						<!-- /.form-group -->

						<div class="form-group">
							<label class="control-sidebar-subheading"> Allow mail
								redirect <input type="checkbox" class="pull-right" checked>
							</label>

							<p>Other sets of options are available</p>
						</div>
						<!-- /.form-group -->

						<div class="form-group">
							<label class="control-sidebar-subheading"> Expose author
								name in posts <input type="checkbox" class="pull-right" checked>
							</label>

							<p>Allow the user to show his name in blog posts</p>
						</div>
						<!-- /.form-group -->

						<h3 class="control-sidebar-heading">Chat Settings</h3>

						<div class="form-group">
							<label class="control-sidebar-subheading"> Show me as
								online <input type="checkbox" class="pull-right" checked>
							</label>
						</div>
						<!-- /.form-group -->

						<div class="form-group">
							<label class="control-sidebar-subheading"> Turn off
								notifications <input type="checkbox" class="pull-right">
							</label>
						</div>
						<!-- /.form-group -->

						<div class="form-group">
							<label class="control-sidebar-subheading"> Delete chat
								history <a href="javascript::;" class="text-red pull-right"><i
									class="fa fa-trash-o"></i></a>
							</label>
						</div>
						<!-- /.form-group -->
					</form>
				</div>
				<!-- /.tab-pane -->
			</div>
		</aside>
		<!-- /.control-sidebar -->
		<!-- Add the sidebar's background. This div must be placed
       immediately after the control sidebar -->
		<div class="control-sidebar-bg"></div>

	</div>
	<!-- ./wrapper -->

	<!-- jQuery 2.1.4 -->
	<script
		src="<c:url value="/resources/plugins/jQuery/jQuery-2.1.4.min.js"/>"></script>
	<!-- Bootstrap 3.3.5 -->
	<script src="<c:url value="/resources/bootstrap/js/bootstrap.min.js"/>"></script>
	<!-- FastClick -->
	<script
		src="<c:url value="/resources/plugins/fastclick/fastclick.js"/>"></script>
	<!-- AdminLTE App -->
	<script src="<c:url value="/resources/js/app.min.js"/>"></script>
	<!-- Sparkline -->
	<script
		src="<c:url value="/resources/plugins/sparkline/jquery.sparkline.min.js" />"></script>

	<!-- SlimScroll 1.3.0 -->
	<script
		src="<c:url value="/resources/plugins/slimScroll/jquery.slimscroll.min.js" />"></script>

	<!-- Google Map API -->
	<script type="text/javascript"
		src="http://maps.google.com/maps/api/js?key=AIzaSyAQGimMUgZU4ww8kD91KRpjZ37c1JabE0g&sensor=false&libraries=drawing"></script>

	<script>
		$(document).ready(
				function() {
					google.maps.event.addDomListener(document
							.getElementById('delete-all-button'), 'click',
							deleteAllShape);
					google.maps.event.addDomListener(document
							.getElementById('draw-geojson'), 'click',
							drawGeojson);
				});
	</script>
	<script>
		var drawingManager;
		var selectedShape;
		var colors = [ '#1E90FF', '#FF1493', '#32CD32', '#FF8C00', '#4B0082' ];
		var selectedColor;
		var colorButtons = {};
		var markers = [];
		var shapes = [];
		var global_map;

		function clearSelection() {
			if (selectedShape) {
				selectedShape.setEditable(false);
				selectedShape = null;
			}
		}

		function setSelection(shape) {
			clearSelection();
			selectedShape = shape;
			shape.setEditable(true);
			selectColor(shape.get('fillColor') || shape.get('strokeColor'));
		}

		function deleteSelectedShape() {
			if (selectedShape) {
				selectedShape.setMap(null);
			}
		}

		function deleteAllShape() {
			var i;
			for (i = 0; i < shapes.length; i++) {
				shapes[i].setMap(null);
			}
			for (i = 0; i < markers.length; i++) {
				markers[i].setMap(null);
			}
		}

		function drawGeojson() {
			var geoobj = JSON.parse($('#geojson').val());
			var shape;
			var i;

			switch (geoobj.type) {
			case 'Point': {
				var latlng = {
					lat : geoobj.coordinates[1],
					lng : geoobj.coordinates[0]
				};
				shape = new google.maps.Marker({
					position : latlng,
					map : global_map
				});
			}
				break;
			case 'Polygon': {
				var ring = geoobj.coordinates[0];
				var ringCoords = [];
				for (i = 0; i < ring.length; i++) {
					ringCoords.push({
						lat : ring[i][1],
						lng : ring[i][0]
					});
				}
				shape = new google.maps.Polygon({
					paths : ringCoords,
					strokeColor : selectedColor,
					strokeOpacity : 0.8,
					strokeWeight : 2,
					fillColor : selectedColor,
					fillOpacity : 0.35,
					map : global_map
				});
			}
				break;
			case 'LineString': {
				var line = geoobj.coordinates;
				var lineCoords = [];
				for (i = 0; i < line.length; i++) {
					lineCoords.push({
						lat : line[i][1],
						lng : line[i][0]
					});
				}
				shape = new google.maps.Polyline({
					path : lineCoords,
					geodesic : true,
					strokeColor : selectedColor,
					strokeOpacity : 1.0,
					strokeWeight : 2,
					map : global_map
				});
			}
				break;
			} // switch ()
			shapes.push(shape);
		}

		function selectColor(color) {
			selectedColor = color;
			for (var i = 0; i < colors.length; ++i) {
				var currColor = colors[i];
				colorButtons[currColor].style.border = currColor == color ? '2px solid #789'
						: '2px solid #fff';
			}
			// Retrieves the current options from the drawing manager and replaces the
			// stroke or fill color as appropriate.
			var polylineOptions = drawingManager.get('polylineOptions');
			polylineOptions.strokeColor = color;
			drawingManager.set('polylineOptions', polylineOptions);
			var rectangleOptions = drawingManager.get('rectangleOptions');
			rectangleOptions.fillColor = color;
			drawingManager.set('rectangleOptions', rectangleOptions);
			var circleOptions = drawingManager.get('circleOptions');
			circleOptions.fillColor = color;
			drawingManager.set('circleOptions', circleOptions);
			var polygonOptions = drawingManager.get('polygonOptions');
			polygonOptions.fillColor = color;
			drawingManager.set('polygonOptions', polygonOptions);
		}

		function setSelectedShapeColor(color) {
			if (selectedShape) {
				if (selectedShape.type == google.maps.drawing.OverlayType.POLYLINE) {
					selectedShape.set('strokeColor', color);
				} else {
					selectedShape.set('fillColor', color);
				}
			}
		}

		function makeColorButton(color) {
			var button = document.createElement('span');
			button.className = 'color-button';
			button.style.backgroundColor = color;
			google.maps.event.addDomListener(button, 'click', function() {
				selectColor(color);
				setSelectedShapeColor(color);
			});
			return button;
		}

		function buildColorPalette() {
			var colorPalette = document.getElementById('color-palette');
			for (var i = 0; i < colors.length; ++i) {
				var currColor = colors[i];
				var colorButton = makeColorButton(currColor);
				colorPalette.appendChild(colorButton);
				colorButtons[currColor] = colorButton;
			}
			selectColor(colors[0]);
		}

		function createMarker(map, value) {
			var myLatLng = {
				lat : value.lat,
				lng : value.lon
			};
			var marker = new google.maps.Marker({
				position : myLatLng,
				map : map,
				title : value.name
			});
			var infowindow = new google.maps.InfoWindow({
				content : value.name
			});
			marker.addListener('click', function() {
				infowindow.open(map, marker);
			});
			return marker;
		}

		function searchWithMarker(map, shape) {
			var radius;
			var count = 10;

			if ($('#nnearest').length) {
				count = $('#nnearest').val();
			}

			radius = {
				lat : shape.getPosition().lat(),
				lon : shape.getPosition().lng(),
				count : count
			};

			$.getJSON("<c:url value="/nnearest?"/>" + $.param(radius), function(data) {
				$.each(data, function(key, value) {
					marker = createMarker(map, value);
					markers.push(marker);
					if ($('#search-list').length) {
						$('#search-list').append(value.name + '<hr/>');
					}
				});
			});
			shapes.push(shape);
		}

		function searchWithRadius(map, shape) {
			var radius = {
				lat : shape.getCenter().lat(),
				lon : shape.getCenter().lng(),
				radius : shape.getRadius()
			};
			$.getJSON("<c:url value="/radius?"/>" + $.param(radius), function(data) {
				$.each(data, function(key, value) {
					marker = createMarker(map, value);
					markers.push(marker);
					if ($('#search-list').length) {
						$('#search-list').append(value.name + '<hr/>');
					}
				});
			});
			shapes.push(shape);
		}

		function searchWithRectangle(map, shape) {
			sw = shape.getBounds().getSouthWest();
			ne = shape.getBounds().getNorthEast();
			latlon = sw.lat() + ',' + sw.lng() + ',' + ne.lat() + ','
					+ sw.lng() + ',' + ne.lat() + ',' + ne.lng() + ','
					+ sw.lat() + ',' + ne.lng() + ',' + sw.lat() + ','
					+ sw.lng();

			$.getJSON("<c:url value="/polygon?latlon="/>" + latlon, function(data) {
				$.each(data, function(key, value) {
					marker = createMarker(map, value);
					markers.push(marker);
					if ($('#search-list').length) {
						$('#search-list').append(value.name + '<hr/>');
					}
				});
			});
			shapes.push(shape);
		}

		function searchWithPolygon(map, shape) {
			var verticles = shape.getPath();
			var latlon = '';
			var xy;
			for (var i = 0; i < verticles.getLength(); i++) {
				xy = verticles.getAt(i);
				latlon += xy.lat() + ',' + xy.lng() + ',';
			}
			xy = verticles.getAt(0);
			latlon += xy.lat() + ',' + xy.lng();
			$.getJSON("<c:url value="/polygon?latlon="/>" + latlon, function(data) {
				$.each(data, function(key, value) {
					marker = createMarker(map, value);
					markers.push(marker);
					if ($('#search-list').length) {
						$('#search-list').append(value.name + '<hr/>');
					}
				});
			});
			shapes.push(shape);
		}

		function searchWithPolyline(map, shape) {
			var verticles = shape.getPath();
			var latlon = '';
			var radius = 100;
			var line;

			for (var i = 0; i < verticles.getLength(); i++) {
				var xy = verticles.getAt(i);
				if (i !== 0) {
					latlon += ',';
				}
				latlon += xy.lat() + ',' + xy.lng();
			}

			if ($('#dwithin').length) {
				radius = $('#dwithin').val();
			}

			line = {
				latlon : latlon,
				radius : radius
			};

			$.getJSON("<c:url value="/polyline?"/>" + $.param(line), function(data) {
				$.each(data, function(key, value) {
					marker = createMarker(map, value);
					markers.push(marker);
					if ($('#search-list').length) {
						$('#search-list').append(value.name + '<hr/>');
					}
				});
			});
			shapes.push(shape);
		}

		function initialize() {
			var map = new google.maps.Map(document.getElementById('map'), {
				zoom : 15,
				center : new google.maps.LatLng(37.566404, 126.985037),
				mapTypeId : google.maps.MapTypeId.ROADMAP,
				disableDefaultUI : true,
				zoomControl : true
			});
			var polyOptions = {
				strokeWeight : 0,
				fillOpacity : 0.45,
				editable : true
			};
			// Creates a drawing manager attached to the map that allows the user to draw
			// markers, lines, and shapes.
			drawingManager = new google.maps.drawing.DrawingManager({
				// drawingMode: google.maps.drawing.OverlayType.POLYGON,
				markerOptions : {
					draggable : true
				},
				polylineOptions : {
					editable : true
				},
				rectangleOptions : polyOptions,
				circleOptions : polyOptions,
				polygonOptions : polyOptions,
				map : map
			});
			google.maps.event
					.addListener(
							drawingManager,
							'overlaycomplete',
							function(e) {
								// Add an event listener that selects the newly-drawn shape when the user
								// mouses down on it.
								var newShape = e.overlay;
								newShape.type = e.type;

								// Switch back to non-drawing mode after drawing a shape.
								drawingManager.setDrawingMode(null);

								if ($('#search-list').length) {
									$('#search-list').empty();
								}

								if (e.type != google.maps.drawing.OverlayType.MARKER) {
									if (e.type == google.maps.drawing.OverlayType.CIRCLE) {
										searchWithRadius(map, newShape);
									} else if (e.type == google.maps.drawing.OverlayType.RECTANGLE) {
										searchWithRectangle(map, newShape);
									} else if (e.type == google.maps.drawing.OverlayType.POLYGON) {
										searchWithPolygon(map, newShape);
									} else if (e.type == google.maps.drawing.OverlayType.POLYLINE) {
										searchWithPolyline(map, newShape);
									}
									google.maps.event.addListener(newShape,
											'click', function() {
												setSelection(newShape);
											});
									setSelection(newShape);
								} else {
									searchWithMarker(map, newShape);
								}
							});
			// Clear the current selection when the drawing mode is changed, or when the
			// map is clicked.
			google.maps.event.addListener(drawingManager,
					'drawingmode_changed', clearSelection);
			google.maps.event.addListener(map, 'click', clearSelection);
			buildColorPalette();
			global_map = map;
		}

		google.maps.event.addDomListener(window, 'load', initialize);
	</script>

</body>
</html>
