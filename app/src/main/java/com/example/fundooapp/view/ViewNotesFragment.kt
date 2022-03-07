package com.example.fundooapp.view

//class ViewNotesFragment : Fragment() {
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var noteAdapter: NoteAdapter
//    private lateinit var noteViewModel: NoteViewModel
//    private lateinit var sharedViewModel: SharedViewModel
//    private var noteList: ArrayList<Notes> = arrayListOf()
//    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
//    private lateinit var staggeredLinearLayoutManager: StaggeredGridLayoutManager
//    private lateinit var notes: Notes
//    private lateinit var archiveNoteFragment: ArchiveNoteFragment
//    private lateinit var nestedScrollView: NestedScrollView
//    private lateinit var progressBar: ProgressBar
//    private var page = 0
//    private var item = 0
//    private var flag = false
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//       val view =  inflater.inflate(R.layout.fragment_view_notes, container, false)
//        recyclerView = view.findViewById(R.id.recycler_view)
//        nestedScrollView = view.findViewById(R.id.scroll_View)
//        progressBar = view.findViewById(R.id.progress_bar)
//        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//        staggeredLinearLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
//        recyclerView.layoutManager = staggeredGridLayoutManager
//        recyclerView.setHasFixedSize(true)
//        noteAdapter = NoteAdapter(requireContext(), noteList)
//        recyclerView.adapter = noteAdapter
//        Log.d("ViewNoteFragment", "${noteAdapter.toString()}")
//        notes = Notes()
//        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService()))[NoteViewModel::class.java]
//        sharedViewModel = ViewModelProvider(this, SharedViewModelFactory(UserAuthService())) [SharedViewModel::class.java]
//        archiveNoteFragment = ArchiveNoteFragment()
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        viewNotes(page, item)
//        pagination()
//        search()
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    private fun viewNotes(page: Int, limit: Int) {
//        if (limit > page) {
//            progressBar.visibility = View.GONE
//            return
//        }
//        noteViewModel.getNotes(noteList, requireContext())
//        noteViewModel.getNoteStatus.observe(viewLifecycleOwner, Observer {
//            noteAdapter.setListData(noteList)
//            noteAdapter.notifyDataSetChanged()
//        })
//    }
//
//    private fun pagination() {
//        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
//                page++
//                progressBar.visibility = View.VISIBLE
//                viewNotes(page, item)
//            }
//        })
//    }
//
//    fun changeView() {
//        if (!flag) {
//            recyclerView.layoutManager = staggeredLinearLayoutManager
//        } else {
//            recyclerView.layoutManager = staggeredGridLayoutManager
//            flag = true
//        }
//    }
//
//    private fun search() {
//        sharedViewModel.queryText.observe(viewLifecycleOwner, Observer { text ->
//            noteAdapter.filter.filter(text)
//        })
//    }
//}

